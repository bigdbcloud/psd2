package com.ibm.api.cashew.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.ibm.api.cashew.beans.APIResponse;


public abstract class APIController
{
	private final Logger logger = LogManager.getLogger(APIController.class);

	protected <T> ResponseEntity<APIResponse<T>> handleException(Throwable e, String version, APIResponse<T> result)
	{
		ResponseEntity<APIResponse<T>> response;
		logger.error(e.getMessage(), e);
		result.setStatus(APIResponse.STATUS_ERROR);
		result.setErrMsg(e.getMessage());
		result.setVersion(version);
		result.setErrDetails(e.getMessage());
		result.setResponse(null);
		response = ResponseEntity.badRequest().body(result);
		return response;
	}
}
