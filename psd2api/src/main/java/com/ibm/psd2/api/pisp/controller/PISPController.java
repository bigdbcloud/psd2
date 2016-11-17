package com.ibm.psd2.api.pisp.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.APIController;
import com.ibm.psd2.api.aip.services.TransactionStatementService;
import com.ibm.psd2.api.integration.kafka.KafkaMessageProducer;
import com.ibm.psd2.api.pisp.service.TransactionRequestService;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

@RestController
public class PISPController extends APIController
{
	private final Logger logger = LogManager.getLogger(PISPController.class);

	@Autowired
	TransactionRequestService txnReqService;

	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	TransactionStatementService tdao;

	@Value("${version}")
	String version;

	@PreAuthorize("hasPermission(#user + '.' + #bankId + '.' + #accountId + '.' + #viewId + '.' + #txnType, 'createTransactionRequest')")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> createTransactionRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) TxnRequest trb,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			if (trb == null || trb.getTo() == null
					|| (accountId.equals(trb.getTo().getAccountId()) && bankId.equals(trb.getTo().getBankId())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			SubscriptionInfo sib = subscriptionService.getSubscriptionInfo(user, (String) auth.getName(), accountId, bankId);
			TxnParty payee = new TxnParty(bankId, accountId);
			TxnRequestDetails t = txnReqService.createTransactionRequest(sib, trb, payee, txnType);

			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("hasPermission(#user + '.' + #bankId + '.' + #accountId + '.' + #viewId + '.' + #txnType, 'createTransactionRequest')")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests/{txnReqId}/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> answerTransactionChallenge(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@PathVariable("txnReqId") String txnReqId, @RequestBody ChallengeAnswer t,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			TxnRequestDetails tdb = txnReqService.answerTransactionRequestChallenge(user,
					viewId, bankId, accountId, txnType, txnReqId, t);

			response = ResponseEntity.ok(tdb);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("hasPermission(#user + '.' + #bankId + '.' + #accountId + '.' + #viewId, 'getTransactionRequestTypes')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionRequestType>> getTransactionRequestTypes(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<List<TransactionRequestType>> response;
		try
		{
			Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();

			SubscriptionInfo sib = subscriptionService.getSubscriptionInfo(user, (String) auth.getName(), accountId, bankId);

			response = ResponseEntity.ok(sib.getTransactionRequestTypes());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("hasPermission(#user + '.' + #bankId + '.' + #accountId + '.' + #viewId, 'getAccountInfo')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TxnRequestDetails>> getTransactionRequests(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<List<TxnRequestDetails>> response;
		try
		{
			List<TxnRequestDetails> txns = txnReqService.getTransactionRequests(user, viewId,
					accountId, bankId);
			response = ResponseEntity.ok(txns);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
	
	@PreAuthorize("hasPermission(#user + '.' + #bankId + '.' + #accountId + '.' + #viewId, 'getAccountInfo')")
	@RequestMapping(method = RequestMethod.PATCH, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transaction/{txnId}/tag/{tag}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Transaction> tagTransaction(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId,
			@PathVariable("txnId") String txnId,
			@PathVariable("tag") String tag,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<Transaction> response;
		try
		{
			Transaction t = tdao.getTransactionById(bankId, accountId, txnId);
			
			if (t == null)
			{
				throw new IllegalArgumentException("Transaction details not found");
			}

			if(tag!=null){
			  
				t.getDetails().setTag(tag);;
			}
			
			t=tdao.updateTransaction(t);
			
			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
