package com.ibm.api.cashew.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.services.UserAccountService;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@RestController
public class UserAccountInformationController extends APIController
{
	private final Logger logger = LogManager.getLogger(UserAccountInformationController.class);

	@Value("${version}")
	private String version;

	@Autowired
	Utils utils;

	@Autowired
	UserAccountService uss;

	@RequestMapping(method = RequestMethod.PUT, value = "/user/{userId}/account/subscriptionRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<SubscriptionRequest>> addBankAccountAndRequestSubscription(@PathVariable("userId") String userId,
			@RequestBody(required = true) SubscriptionRequest subscriptionRequest)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		logger.debug("Creating psd2 bank account for user = " + auth.getName());
		APIResponse<SubscriptionRequest> result = null;
		ResponseEntity<APIResponse<SubscriptionRequest>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(uss.subscribe(auth.getName(), subscriptionRequest));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user/{userId}/account/subscription/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<UserAccount>> answerSubscriptionChallenge(@PathVariable("userId") String userId,
			@RequestBody(required = true) SubscriptionChallengeAnswer sca)
	{
		logger.debug("Answering Challenge for psd2 bank account for user = " + sca.getAppUsername());
		APIResponse<UserAccount> result = null;
		ResponseEntity<APIResponse<UserAccount>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(uss.answerSubscriptionRequestChallenge(sca));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}/{bankId}/{accountId}/{viewId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<BankAccountDetailsView>> getAccountDetails(@PathVariable("userId") String userId, 
			@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId)
	{
		logger.debug("Getting Account Details for user = " + userId);
		APIResponse<BankAccountDetailsView> result = null;
		ResponseEntity<APIResponse<BankAccountDetailsView>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(uss.getAccountInformation(userId, bankId, accountId, viewId));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}
}
