package com.ibm.psd2.oauth2server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.oauth2server.beans.ClientInfo;
import com.ibm.psd2.oauth2server.beans.UserInfo;
import com.ibm.psd2.oauth2server.services.ClientDetailsServiceImpl;
import com.ibm.psd2.oauth2server.services.UserDetailsServiceImpl;

@RestController
public class UserController
{
	@Autowired
	UserDetailsServiceImpl uds;

	@Autowired
	ClientDetailsServiceImpl cds;

	@Value("${user.mobileNumber}")
	private String twilioPhnNumber;

	@RequestMapping(method = RequestMethod.POST, value = "/admin/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
	public ResponseEntity<String> createUser(@RequestBody(required = true) UserInfo u)
	{
		ResponseEntity<String> response;
		try
		{
			u.setMobileNumber(twilioPhnNumber);
			uds.createUserInfo(u);
			response = ResponseEntity.ok("SUCCESS");
		}
		catch (Exception e)
		{
			response = ResponseEntity.badRequest().body(e.getMessage());
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.ALL_VALUE)
	public ResponseEntity<String> createClient(@RequestBody(required = true) ClientInfo c)
	{
		ResponseEntity<String> response;
		try
		{
			cds.createClient(c);
			response = ResponseEntity.ok("SUCCESS");
		}
		catch (Exception e)
		{
			response = ResponseEntity.badRequest().body(e.getMessage());
		}
		return response;
	}

}
