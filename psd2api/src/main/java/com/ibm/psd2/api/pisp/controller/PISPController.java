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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.integration.KafkaMessageProducer;
import com.ibm.psd2.api.pisp.service.TransactionRequestService;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.commons.controller.APIController;
import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.pisp.TxnParty;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequest;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.datamodel.subscription.TransactionRequestType;
import com.ibm.psd2.commons.datamodel.subscription.ViewId;

@RestController
public class PISPController extends APIController
{
	private static final Logger logger = LogManager.getLogger(PISPController.class);

	@Autowired
	TransactionRequestService pdao;

	@Autowired
	SubscriptionService sdao;

	@Autowired
	KafkaMessageProducer kmp;

	@Value("${version}")
	String version;

	@Value("${kakfa.topic}")
	String topic;

	@Value("${psd2api.integration.useKafka}")
	private boolean useKafka;

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> createTransactionRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) TxnRequest trb, Authentication auth)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();

			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			if (trb == null || trb.getTo() == null
					|| (accountId.equals(trb.getTo().getAccountId()) && bankId.equals(trb.getTo().getBankId())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			TxnParty payee = new TxnParty(bankId, accountId);
			TxnRequestDetails t = pdao.createTransactionRequest(sib, trb, payee, txnType);

			if (useKafka && t != null && TxnRequestDetails.TXN_STATUS_PENDING.equalsIgnoreCase(t.getStatus()))
			{
				kmp.publishMessage(topic, t.getId(), t.toString());
			}

			response = ResponseEntity.ok(t);
		} catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests/{txnReqId}/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> answerTransactionChallenge(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@PathVariable("txnReqId") String txnReqId, @RequestBody ChallengeAnswer t, Authentication auth)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			TxnRequestDetails tdb = pdao.answerTransactionRequestChallenge(user, viewId, bankId, accountId, txnType,
					txnReqId, t);

			if (useKafka && tdb != null && TxnRequestDetails.TXN_STATUS_PENDING.equalsIgnoreCase(tdb.getStatus()))
			{
				kmp.publishMessage(topic, tdb.getId(), tdb.toString());
			}

			response = ResponseEntity.ok(tdb);
		} catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionRequestType>> getTransactionRequestTypes(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, Authentication auth)
	{
		ResponseEntity<List<TransactionRequestType>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);

			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			response = ResponseEntity.ok(sib.getTransactionRequestTypes());
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TxnRequestDetails>> getTransactionRequests(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, Authentication auth)
	{
		ResponseEntity<List<TxnRequestDetails>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			List<TxnRequestDetails> txns = pdao.getTransactionRequests(user, viewId, accountId, bankId);
			response = ResponseEntity.ok(txns);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
