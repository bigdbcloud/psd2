package com.ibm.psd2.api.subscription.controller;

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

import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.commons.subscription.service.SubscriptionRequestService;

@RestController
public class SubscriptionRequestController
{

	@Autowired
	SubscriptionRequestService subsReqService;

	private static final Logger logger = LogManager.getLogger(SubscriptionRequestController.class);

	@Value("${version}")
	private String version;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SubscriptionRequest> createSubscription(
			@RequestBody(required=true) SubscriptionRequest s)
	{
		ResponseEntity<SubscriptionRequest> response;
		try
		{
			SubscriptionRequest sreturn = subsReqService.createSubscriptionRequest(s);
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
