package com.ibm.api.cashew.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.Goal;
import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.services.GoalService;

@RestController
public class GoalController extends APIController
{
	private final Logger logger = LogManager.getLogger(PaymentsController.class);

	@Value("${version}")
	private String version;

	@Autowired
	GoalService goalService;

	@PreAuthorize("#goal.username == authentication.name")
	@RequestMapping(method = RequestMethod.PUT, value = "/goal", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Goal>> createGoal(@RequestBody(required = true) Goal goal)
	{

		APIResponse<Goal> result = null;
		ResponseEntity<APIResponse<Goal>> response;
		try
		{
			result = new APIResponse<>();
			Goal g = goalService.saveGoal(goal);
			result.setResponse(g);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/goals", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<List<Goal>>> getUserInfo()
	{
		APIResponse<List<Goal>> result = new APIResponse<>();
		ResponseEntity<APIResponse<List<Goal>>> response;
		try
		{
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			List<Goal> goals = goalService.getGoals(userId);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(goals);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}
}
