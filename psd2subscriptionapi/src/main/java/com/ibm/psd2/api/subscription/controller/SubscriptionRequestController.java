package com.ibm.psd2.api.subscription.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.subscription.service.SubscriptionRequestService;
import com.ibm.psd2.api.subscription.service.SubscriptionRules;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.SimpleResponse;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@RestController
public class SubscriptionRequestController
{

	@Autowired
	SubscriptionRequestService subsReqService;

	@Autowired
	SubscriptionService subsService;
	
	@Autowired
	SubscriptionRules srules;

	private static final Logger logger = LogManager.getLogger(SubscriptionRequestController.class);

	@Value("${version}")
	private String version;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<SubscriptionInfo>> getSubscriptionInfo()
	{
		ResponseEntity<List<SubscriptionInfo>> response;
		try
		{
			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			List<SubscriptionInfo> sreturn = subsService.getSubscriptionInfo((String) auth.getPrincipal());
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
	
	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.PATCH, value = "/subscription/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponse> activateSubscription(@PathVariable("id") String id,
			@RequestBody(required = true) ChallengeAnswer cab)
	{
		ResponseEntity<SimpleResponse> response;
		SimpleResponse srb = new SimpleResponse();
		try
		{
			SubscriptionRequest sr = subsReqService.getSubscriptionRequestByIdAndChallenge(id, cab);
			if (sr == null)
			{
				throw new IllegalArgumentException("Subscription Request Not Found for id = " + id + " , challenge.id = " + cab.getId());
			}
			
			if (!srules.validateTxnChallengeAnswer(cab))
			{
				throw new IllegalArgumentException("Challenge Answer is not correct");
			}
			
			subsService.createSubscriptionInfo(sr.getSubscriptionInfo());
			subsReqService.updateSubscriptionRequestStatus(id, SubscriptionRequest.STATUS_SUBSCRIBED);
			srb.setResponseCode(SimpleResponse.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
			
		} catch (Exception e)
		{
			logger.error(e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}
	
}
