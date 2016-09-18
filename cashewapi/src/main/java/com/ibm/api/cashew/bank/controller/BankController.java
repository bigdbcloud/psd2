package com.ibm.api.cashew.bank.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.bank.service.BankService;
import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.controllers.APIController;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;

@RestController
public class BankController extends APIController {

	private static final Logger logger = LogManager.getLogger(BankController.class);

	@Autowired
	private BankService bankservice;

	@Value("${version}")
	private String version;

	@RequestMapping(method = RequestMethod.POST, value = "/banks/{bankId}/accounts/{accountId}/transaction-type/{txnType}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<TxnRequestDetails>> postTransaction(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("txnType") String txnType, @RequestBody(required = true) TxnRequest txnReq) {

		APIResponse<TxnRequestDetails> result = null;
		ResponseEntity<APIResponse<TxnRequestDetails>> response;

		try {
			if (txnReq == null || txnReq.getTo() == null
					|| (accountId.equals(txnReq.getTo().getAccountId()) && bankId.equals(txnReq.getTo().getBankId()))) {
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			TxnParty payer = new TxnParty(bankId, accountId);
			TxnRequestDetails t = bankservice.createTransaction(txnReq, payer, txnType, auth.getName());

			result = new APIResponse<>();
			result.setResponse(t);
			response = ResponseEntity.ok(result);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<APIResponse<List<Transaction>>> getTransactions(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId,
			@RequestHeader(value = "obp_sort_direction", required = false) String sortDirection,
			@RequestHeader(value = "obp_limit", required = false) Integer limit,
			@RequestHeader(value = "obp_from_date", required = false) String fromDate,
			@RequestHeader(value = "obp_to_date", required = false) String toDate,
			@RequestHeader(value = "obp_sort_by", required = false) String sortBy,
			@RequestHeader(value = "obp_offset", required = false) Integer offset,
			@RequestHeader(value = "user", required = true) String user) {
		
		
		APIResponse<List<Transaction>> result = null;
		ResponseEntity<APIResponse<List<Transaction>>> response;
		try {
			
			List<Transaction> tranList = bankservice.getTransactions(user, accountId, bankId, fromDate, toDate, sortBy, offset,
					limit);

			result = new APIResponse<>();
			result.setResponse(tranList);
			response = ResponseEntity.ok(result);
			
		} catch (Exception ex) {
			
			logger.error(ex.getMessage(), ex);
			response = handleException(ex, version, result);
		}
		return response;
	}

}
