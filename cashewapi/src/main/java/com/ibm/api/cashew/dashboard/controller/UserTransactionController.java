package com.ibm.api.cashew.dashboard.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.controllers.APIController;
import com.ibm.api.cashew.dashboard.services.UserTransactionService;
import com.ibm.api.cashew.voucher.controller.VoucherController;

@RestController
public class UserTransactionController extends APIController {

	private final Logger logger = LogManager.getLogger(VoucherController.class);

	@Value("${version}")
	private String version;

	@Autowired
	private UserTransactionService userTxnService;

	@RequestMapping(method = RequestMethod.GET, value = "{userId}/{bankId}/transaction/distribution", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<List<AggregationResponse>>> getUserTxnDistribution(
			@PathVariable(value = "userId") String userId, @PathVariable(value = "bankId") String bankId,
			@RequestParam(value = "accountId", required = false) String accountId,
			@RequestHeader(value = "fromDate", required = false) String fromDate,
			@RequestHeader(value = "toDate", required = false) String toDate)

	{

		APIResponse<List<AggregationResponse>> result = new APIResponse<>();
		ResponseEntity<APIResponse<List<AggregationResponse>>> response;

		try {

			List<AggregationResponse> aggrResponse = userTxnService.getUserTxnDistribution(userId, bankId, accountId);
			result.setResponse(aggrResponse);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);

		}

		return response;

	}

	@RequestMapping(method = RequestMethod.GET, value = "{userId}/{bankId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<List<AggregationResponse>>> getUserTxnHistogram(
			@PathVariable(value = "userId") String userId, @PathVariable(value = "bankId") String bankId,
			@RequestParam(value = "accountId", required = false) String accountId,
			@RequestHeader(value = "fromDate", required = false) String fromDate,
			@RequestHeader(value = "toDate", required = false) String toDate)

	{

		APIResponse<List<AggregationResponse>> result = new APIResponse<>();
		ResponseEntity<APIResponse<List<AggregationResponse>>> response;

		try {

			List<AggregationResponse> aggrResponse=userTxnService.getUserAvgTxnDistribution(userId, bankId, accountId, fromDate, toDate);
			result.setResponse(aggrResponse);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);

		}

		return response;

	}
	
	

}
