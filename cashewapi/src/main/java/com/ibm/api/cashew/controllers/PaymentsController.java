package com.ibm.api.cashew.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.api.cashew.beans.APIResponse;
import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.services.PaymentsService;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.pisp.CounterParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

@RestController
public class PaymentsController extends APIController
{
	private final Logger logger = LogManager.getLogger(PaymentsController.class);

	@Value("${version}")
	private String version;

	@Autowired
	Utils utils;
	
	@Autowired
	PaymentsService paymentService;

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}/{bankId}/{accountId}/transaction-request-types", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<List<TransactionRequestType>>> getTxnRequestTypes(
			@PathVariable("userId") String userId, @PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId)
	{
		logger.debug("Getting Txn Request Types for user = " + userId);
		APIResponse<List<TransactionRequestType>> result = null;
		ResponseEntity<APIResponse<List<TransactionRequestType>>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(paymentService.getTransactionRequestTypes(userId, bankId, accountId));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}/{bankId}/{accountId}/payees", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<List<CounterParty>>> getPayees(
			@PathVariable("userId") String userId, 
			@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId)
	{
		logger.debug("Getting Txn Request Types for user = " + userId);
		APIResponse<List<CounterParty>> result = null;
		ResponseEntity<APIResponse<List<CounterParty>>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(paymentService.getPayees(userId, bankId, accountId));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{userId}/{bankId}/{accountId}/payment", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<TxnRequestDetails>> makePayment(
			@PathVariable("userId") String userId, @PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId,
			@RequestBody(required = true)TxnRequest txnRequest)
	{
		logger.debug("Getting Txn Request Types for user = " + userId);
		APIResponse<TxnRequestDetails> result = null;
		ResponseEntity<APIResponse<TxnRequestDetails>> response;
		try
		{
			result = new APIResponse<>();
			result.setResponse(paymentService.createTransactionRequest(userId, bankId, accountId, txnRequest));
			response = ResponseEntity.ok(result);
		}
		catch (Exception e)
		{
			response = handleException(e, version, result);
		}
		return response;
	}
	

	@RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/{bankId}/{accountId}/transaction/{txnId}/tag/{tag}", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<com.ibm.api.cashew.beans.Transaction>> tagTransaction(@PathVariable("userId") String userId,
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("txnId") String txnId, @PathVariable("tag") String tag) {

		APIResponse<com.ibm.api.cashew.beans.Transaction> result = null;
		ResponseEntity<APIResponse<com.ibm.api.cashew.beans.Transaction>> response;
		try {

			com.ibm.api.cashew.beans.Transaction txn = paymentService.tagTransaction(userId, bankId, accountId, txnId, tag);

			result = new APIResponse<>();
			result.setResponse(txn);
			response = ResponseEntity.ok(result);

		} catch (Exception ex) {

			logger.error(ex.getMessage(), ex);
			response = handleException(ex, version, result);
		}
		return response;
	}
	
	
	@RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/{bankId}/{accountId}/transaction/{txnReqType}/{txnId}/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("authentication.name == #userId")
	public @ResponseBody ResponseEntity<APIResponse<TxnRequestDetails>> answerTransactionChallenge(
			@PathVariable("userId") String userId, 
			@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId,
			@PathVariable("txnReqType") String txnReqType,
			@PathVariable("txnId") String txnId,			
			@RequestBody(required = true) ChallengeAnswer ca) {
		
		logger.debug("answering Challenge for transaction: {}",txnId);
		
		APIResponse<TxnRequestDetails> result = null;
		ResponseEntity<APIResponse<TxnRequestDetails>> response;
		try {
			result = new APIResponse<>();
			result.setResponse(paymentService.answerTxnChallnge(userId,bankId,accountId,txnReqType,txnId,ca));
			response = ResponseEntity.ok(result);
		} catch (Exception e) {
			response = handleException(e, version, result);
		}
		return response;
	}
}
