package com.ibm.api.cashew.controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.Reminder;
import com.ibm.api.cashew.services.ReminderService;

@RestController
public class ReminderController extends APIController {

	private final Logger logger = LogManager.getLogger(ReminderController.class);

	@Value("${version}")
	private String version;

	@Autowired
	private ReminderService reminderService;

	
	@RequestMapping(method = RequestMethod.PUT, value = "/reminder", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Reminder>> createReminder(
			@RequestBody(required = true) Reminder reminder) {

		APIResponse<Reminder> result = null;
		ResponseEntity<APIResponse<Reminder>> response;
		try {

			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			result = new APIResponse<>();

			if (StringUtils.isBlank(reminder.getCreatedBy())) {
				reminder.setCreatedBy(auth.getName());
			}

			Reminder remRes = reminderService.createReminder(reminder);
			result.setResponse(remRes);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/reminder", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Reminder>> updateReminder(
			@RequestBody(required = true) Reminder reminder) {

		APIResponse<Reminder> result = null;
		ResponseEntity<APIResponse<Reminder>> response;
		try {

			result = new APIResponse<>();
			Reminder remRes = reminderService.updateReminder(reminder);
			result.setResponse(remRes);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/reminder/{reminderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Reminder>> deleteReminder(@PathVariable String reminderId) {

		APIResponse<Reminder> result = null;
		ResponseEntity<APIResponse<Reminder>> response;
		try {

			result = new APIResponse<>();
			reminderService.deleteReminder(reminderId);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/reminder", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<List<Reminder>>> getReminders(
			@RequestHeader(required = false) String fromDate,
			@RequestHeader(required = false) String toDate) {

		APIResponse<List<Reminder>> result = null;
		ResponseEntity<APIResponse<List<Reminder>>> response;
		try {

			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			result = new APIResponse<>();
			
			List<Reminder> remList=reminderService.getReminders(auth.getName(), fromDate, toDate);
			result.setResponse(remList);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

}
