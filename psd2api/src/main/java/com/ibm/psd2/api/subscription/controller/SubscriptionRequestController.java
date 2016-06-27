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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.subscription.dao.SubscriptionDao;
import com.ibm.psd2.api.subscription.dao.SubscriptionRequestDao;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfoBean;
import com.ibm.psd2.commons.beans.subscription.SubscriptionRequestBean;

@RestController
public class SubscriptionRequestController
{

	@Autowired
	SubscriptionRequestDao srdao;

	@Autowired
	SubscriptionDao sdao;

	private static final Logger logger = LogManager.getLogger(SubscriptionRequestController.class);

	@Value("${version}")
	private String version;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "/subscription/request", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SubscriptionRequestBean> createSubscription(
			@RequestBody(required=true) SubscriptionRequestBean s)
	{
		ResponseEntity<SubscriptionRequestBean> response;
		try
		{
			SubscriptionRequestBean sreturn = srdao.createSubscriptionRequest(s);
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
	public @ResponseBody ResponseEntity<List<SubscriptionInfoBean>> getSubscriptionInfo(Authentication auth)
	{
		ResponseEntity<List<SubscriptionInfoBean>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			List<SubscriptionInfoBean> sreturn = sdao.getSubscriptionInfo((String) oauth2.getPrincipal(), oauth2.getOAuth2Request().getClientId());
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
