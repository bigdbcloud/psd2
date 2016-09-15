package com.ibm.api.cashew.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.services.UserService;

@RestController
@RequestMapping
public class UserController extends APIController
{
	private final Logger logger = LogManager.getLogger(UserController.class);

	@Value("${version}")
	private String version;

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> getUserInfo()
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			logger.debug("Principal = " + auth.getPrincipal());
			result = new APIResponse();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(auth.getPrincipal());
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("#user.userId == authentication.name || hasRole('ADMIN')")
	public @ResponseBody ResponseEntity<APIResponse> updateUser(@RequestBody(required = true) User user)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			User res = userService.updateUser(user);
			result = new APIResponse();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}
}
