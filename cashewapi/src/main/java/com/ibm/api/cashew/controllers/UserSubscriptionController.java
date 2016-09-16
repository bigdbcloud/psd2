package com.ibm.api.cashew.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.AccountSubscription;
import com.ibm.api.cashew.services.UserSubscriptionService;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@RestController
public class UserSubscriptionController extends APIController
{
	private final Logger logger = LogManager.getLogger(UserController.class);

	@Value("${version}")
	private String version;

	@Autowired
	UserSubscriptionService uss;

	@RequestMapping(method = RequestMethod.PUT, value = "/user/{userId}/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<SubscriptionRequest>> getUserInfo(@RequestBody(required = true) AccountSubscription subscription)
	{
		APIResponse<SubscriptionRequest> result = null;
		ResponseEntity<APIResponse<SubscriptionRequest>> response;
		try
		{
			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			logger.debug("Principal = " + auth.getPrincipal());
			result = new APIResponse<>();
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}
	
}
