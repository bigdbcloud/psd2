package com.ibm.psd2.api.subscription.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.subscription.service.SubscriptionRequestService;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;

@RestController
public class SubscriptionRequestController
{

	@Autowired
	SubscriptionRequestService srdao;

	@Autowired
	SubscriptionService sdao;

	private static final Logger logger = LogManager.getLogger(SubscriptionRequestController.class);

	@Value("${version}")
	private String version;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "/subscription/request", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SubscriptionRequest> createSubscription(
			@RequestBody(required=true) SubscriptionRequest s)
	{
		ResponseEntity<SubscriptionRequest> response;
		try
		{
			SubscriptionRequest sreturn = srdao.createSubscriptionRequest(s);
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<SubscriptionInfo>> getSubscriptionInfo(Authentication auth)
	{
		//SecurityContextHolder.getContext().getAuthentication()
		ResponseEntity<List<SubscriptionInfo>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			List<SubscriptionInfo> sreturn = sdao.getSubscriptionInfo((String) oauth2.getPrincipal(), oauth2.getOAuth2Request().getClientId());
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
