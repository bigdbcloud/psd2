package com.ibm.api.cashew.vocher.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.Vocher;
import com.ibm.api.cashew.controllers.APIController;
import com.ibm.api.cashew.vocher.service.VocherService;

@RestController
public class VocherController extends APIController {

	private final Logger logger = LogManager.getLogger(VocherController.class);

	@Value("${version}")
	private String version;

	@Autowired
	private VocherService vocherService;

	@RequestMapping(method = RequestMethod.POST, value = "/vocher", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Vocher>> createVocher(
			@RequestBody(required = true) Vocher vocher) {

		APIResponse<Vocher> result = null;
		ResponseEntity<APIResponse<Vocher>> response;
		try {

			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			result = new APIResponse<>();

			if (StringUtils.isBlank(vocher.getCreatedBy())) {
				vocher.setCreatedBy(auth.getName());
			}

			Vocher vocherRes = vocherService.createVocher(vocher);

			result.setResponse(vocherRes);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/vocher", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Vocher>> redeemVocher(@RequestBody(required = true) Vocher vocher) {

		APIResponse<Vocher> result = null;
		ResponseEntity<APIResponse<Vocher>> response;
		try {

			result = new APIResponse<>();
			Vocher remRes = vocherService.redeemVocher(vocher);
			result.setResponse(remRes);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}
}
