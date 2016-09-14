package com.ibm.api.oauth2server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.oauth2server.beans.ClientInfo;
import com.ibm.api.oauth2server.beans.UserInfo;
import com.ibm.api.oauth2server.services.ClientDetailsServiceImpl;
import com.ibm.api.oauth2server.services.UserDetailsServiceImpl;

@RestController
public class UserController
{
	@Autowired
	UserDetailsServiceImpl uds;
	
	@Autowired
	ClientDetailsServiceImpl cds;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/user", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.ALL_VALUE)
	public ResponseEntity<String> createUser(@RequestBody UserInfo u)
	{
		ResponseEntity<String> response;
		try
		{
			uds.createUserInfo(u);
			response = ResponseEntity.ok("SUCCESS");
		}
		catch (Exception e)
		{
			response = ResponseEntity.badRequest().body(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/client", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.ALL_VALUE)
	public ResponseEntity<String> createClient(@RequestBody ClientInfo c)
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
