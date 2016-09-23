package com.ibm.api.cashew.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.Insight;
import com.ibm.api.cashew.services.UserInsightsService;

@RestController
public class UserInsightsController  extends APIController
{
	private final Logger logger = LogManager.getLogger(VoucherController.class);

	@Value("${version}")
	private String version;

	@Autowired
	UserInsightsService uis;
	
	@RequestMapping(method = RequestMethod.GET, value = "/user/expenseInsightsForAgeGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<List<Insight>>> getUserInfo() {
		APIResponse<List<Insight>> result = new APIResponse<>();
		ResponseEntity<APIResponse<List<Insight>>> response;
		try {
			String userId = SecurityContextHolder.getContext().getAuthentication().getName();
			List<Insight> insights = uis.getAvgSpendInAgeGroup(userId);
			result.setStatus(APIResponse.STATUS_SUCCESS);
			result.setResponse(insights);
			result.setVersion(version);
			response = ResponseEntity.ok(result);
			
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}
}
