package com.ibm.api.cashew.controllers;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.Tag;
import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.services.UserService;
import com.ibm.api.cashew.utils.Utils;

@RestController
public class UserController extends APIController {
	private final Logger logger = LogManager.getLogger(UserController.class);

	@Value("${version}")
	private String version;

	@Autowired
	UserService userService;

	@Autowired
	Utils utils;

	@RequestMapping(method = RequestMethod.GET, value = "/user/profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<User>> getUserInfo() {
		APIResponse<User> result = new APIResponse<>();
		ResponseEntity<APIResponse<User>> response;
		try {
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userService.findUserById(userId);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(user);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
			
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("#user.userId == authentication.name || hasRole('ADMIN')")
	public @ResponseBody ResponseEntity<APIResponse<User>> updateUser(@RequestBody(required = true) User user) {
		APIResponse<User> result = new APIResponse<>();
		ResponseEntity<APIResponse<User>> response;
		try {
			User res = userService.updateUser(user);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user/{userId}/phone/{phone}/", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("#userId == authentication.name || hasRole('ADMIN')")
	public @ResponseBody ResponseEntity<APIResponse<Long>> changePhone(@PathVariable("userId") String userId,
			@PathVariable("phone") String phone) {
		APIResponse<Long> result = new APIResponse<>();
		ResponseEntity<APIResponse<Long>> response;

		logger.info("changing phone of user: " + userId);
		try {
			Long res = userService.changePhone(userId, phone);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user/{userId}/email/{email}/", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("#userId == authentication.name || hasRole('ADMIN')")
	public @ResponseBody ResponseEntity<APIResponse<Long>> changeEmail(@PathVariable("userId") String userId,
			@PathVariable("email") String email) {
		APIResponse<Long> result = new APIResponse<>();
		ResponseEntity<APIResponse<Long>> response;

		logger.info("changing email of user: " + userId);
		try {
			Long res = userService.changeEmail(userId, email);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user/{userId}/dob/{dob}/", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("#userId == authentication.name || hasRole('ADMIN')")
	public @ResponseBody ResponseEntity<APIResponse<Long>> changeDateOfBirth(@PathVariable("userId") String userId,
			@PathVariable("dob") String dob) {
		APIResponse<Long> result = new APIResponse<>();
		ResponseEntity<APIResponse<Long>> response;

		logger.info("changing date of birth of user: " + userId);
		try {
			Long res = userService.changeDOB(userId,dob);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/user/{userId}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Long>> addTags(@PathVariable("userId") String userId,
			@RequestBody(required = true) Set<Tag> tags) {

		APIResponse<Long> result = new APIResponse<>();
		ResponseEntity<APIResponse<Long>> response;

		logger.info("Adding tags for user: " + userId);

		try {

			Long res = userService.addTags(tags, userId);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		
		return response;

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Set<String>>> getTags() {

		APIResponse<Set<String>> result = new APIResponse<>();
		ResponseEntity<APIResponse<Set<String>>> response;


		try {

			Set<String> res = utils.getTags();
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(res);
			result.setVersion(version);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		
		return response;

	}
}
