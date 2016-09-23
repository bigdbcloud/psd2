package com.ibm.big.oauth2server.controller;

import java.security.Principal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.big.oauth2server.beans.APIResponse;
import com.ibm.big.oauth2server.beans.Client;
import com.ibm.big.oauth2server.beans.SocialLoginProvider;
import com.ibm.big.oauth2server.beans.User;
import com.ibm.big.oauth2server.services.ClientService;
import com.ibm.big.oauth2server.services.SocialLoginProviderService;
import com.ibm.big.oauth2server.services.UserService;



@RestController
public class AuthServerController extends APIController
{
	private static final Logger logger = LogManager.getLogger(AuthServerController.class);

	@Autowired
	UserService userService;

	@Autowired
	ClientService clientService;
	
	@Autowired
	SocialLoginProviderService socialLoginProviderService;

	@Value("${version}")
	private String version;
	
	@Value("${user.mobileNumber}")
	private String twilioPhnNumber;

	@RequestMapping("/userinfo")
	public Principal user(Principal principal)
	{
		return principal;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> addUser(@RequestBody(required = true) User user)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			logger.debug("Creating User: " + user);
			result = new APIResponse();
			user.setMobileNumber(twilioPhnNumber);
			User res = userService.createUser(user);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> addClient(@RequestBody(required = true) Client client)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			logger.debug("Creating client: " + client);
			result = new APIResponse();
			Client c = clientService.createClient(client);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(c);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/provider", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse> addProvider(@RequestBody(required = true) SocialLoginProvider provider)
	{
		APIResponse result = null;
		ResponseEntity<APIResponse> response;
		try
		{
			logger.debug("Creating provider: " + provider);
			result = new APIResponse();
			SocialLoginProvider c = socialLoginProviderService.save(provider);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(c);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e)
		{
			response = handleException(e, version);
		}
		return response;
	}
}
