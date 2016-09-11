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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.APIController;
import com.ibm.psd2.api.integration.KafkaMessageProducer;
import com.ibm.psd2.api.pisp.service.TransactionRequestService;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.pisp.TxnParty;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequest;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.datamodel.subscription.TransactionRequestType;

@RestController
public class PISPController extends APIController
{
	private static final Logger logger = LogManager.getLogger(PISPController.class);

	@Autowired
	TransactionRequestService txnReqService;

	@Autowired
	SubscriptionService subscriptionService;

	@Autowired
	KafkaMessageProducer kmp;

	@Value("${version}")
	String version;

	@Value("${psd2api.integration.kakfa.topic}")
	String topic;

	@Value("${psd2api.integration.useKafka}")
	private boolean useKafka;

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> createTransactionRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) TxnRequest trb)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			if (trb == null || trb.getTo() == null
					|| (accountId.equals(trb.getTo().getAccountId()) && bankId.equals(trb.getTo().getBankId())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			SubscriptionInfo sib = subscriptionService.getSubscriptionInfo((String) auth.getPrincipal(),
					auth.getOAuth2Request().getClientId(), accountId, bankId);
			TxnParty payee = new TxnParty(bankId, accountId);
			TxnRequestDetails t = txnReqService.createTransactionRequest(sib, trb, payee, txnType);

			if (useKafka && t != null && TxnRequestDetails.TXN_STATUS_PENDING.equalsIgnoreCase(t.getStatus()))
			{
				kmp.publishMessage(topic, t.getId(), t.toString());
			}

			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests/{txnReqId}/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> answerTransactionChallenge(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@PathVariable("txnReqId") String txnReqId, @RequestBody ChallengeAnswer t)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();

			TxnRequestDetails tdb = txnReqService.answerTransactionRequestChallenge((String) auth.getPrincipal(),
					viewId, bankId, accountId, txnType, txnReqId, t);

			if (useKafka && tdb != null && TxnRequestDetails.TXN_STATUS_PENDING.equalsIgnoreCase(tdb.getStatus()))
			{
				kmp.publishMessage(topic, tdb.getId(), tdb.toString());
			}

			response = ResponseEntity.ok(tdb);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionRequestType>> getTransactionRequestTypes(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId)
	{
		ResponseEntity<List<TransactionRequestType>> response;
		try
		{
			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

			SubscriptionInfo sib = subscriptionService.getSubscriptionInfo((String) auth.getPrincipal(),
					auth.getOAuth2Request().getClientId(), accountId, bankId);

			response = ResponseEntity.ok(sib.getTransactionRequestTypes());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TxnRequestDetails>> getTransactionRequests(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId)
	{
		ResponseEntity<List<TxnRequestDetails>> response;
		try
		{
			Authentication auth = (Authentication) SecurityContextHolder.getContext().getAuthentication();
			List<TxnRequestDetails> txns = txnReqService.getTransactionRequests((String) auth.getPrincipal(), viewId, accountId, bankId);
			response = ResponseEntity.ok(txns);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
