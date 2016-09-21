package com.ibm.api.cashew.controllers;

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
import com.ibm.api.cashew.beans.Voucher;
import com.ibm.api.cashew.services.VoucherService;

@RestController
public class VoucherController extends APIController {

	private final Logger logger = LogManager.getLogger(VoucherController.class);

	@Value("${version}")
	private String version;

	@Autowired
	private VoucherService voucherService;

	@RequestMapping(method = RequestMethod.POST, value = "/voucher", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Voucher>> createVoucher(
			@RequestBody(required = true) Voucher vocher) {

		APIResponse<Voucher> result = null;
		ResponseEntity<APIResponse<Voucher>> response;
		try {

			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			result = new APIResponse<>();
			vocher.setCreatedBy(auth.getName());

			Voucher vocherRes = voucherService.createVocher(vocher);

			result.setResponse(vocherRes);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/voucher", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<Voucher>> redeemVoucher(@RequestBody(required = true) Voucher voucher) {

		APIResponse<Voucher> result = null;
		ResponseEntity<APIResponse<Voucher>> response;
		try {

			result = new APIResponse<>();
			Voucher remRes = voucherService.redeemVocher(voucher);
			result.setResponse(remRes);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}
}
