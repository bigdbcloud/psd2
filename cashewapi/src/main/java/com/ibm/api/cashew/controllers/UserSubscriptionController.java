package com.ibm.api.cashew.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.services.UserSubscriptionService;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@RestController
public class UserSubscriptionController extends APIController
{
	private Logger logger = LogManager.getLogger(UserController.class);

	@Value("${version}")
	private String version;

	@Autowired
	UserSubscriptionService uss;

	@RequestMapping(method = RequestMethod.PUT, value = "/user/account/subscriptionRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userAccount.appUsername")
	public @ResponseBody ResponseEntity<APIResponse<SubscriptionRequest>> createBankAccountAndSubscriptionRequest(
			@RequestBody(required = true) UserAccount userAccount)
	{
		logger.debug("Creating psd2 bank account for user = " + userAccount.getAppUsername());
		APIResponse<SubscriptionRequest> result = null;
		ResponseEntity<APIResponse<SubscriptionRequest>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(uss.subscribe(userAccount));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/user/account/subscriptionRequest", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #sca.appUsername")
	public @ResponseBody ResponseEntity<APIResponse<UserAccount>> answerSubscriptionChallenge(
			@RequestBody(required = true) SubscriptionChallengeAnswer sca)
	{
		logger.debug("Creating psd2 bank account for user = " + sca.getAppUsername());
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
}
