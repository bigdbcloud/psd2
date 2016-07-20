package com.ibm.psd2.api.pisp.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.commons.integration.KafkaMessageProducer;
import com.ibm.psd2.api.pisp.dao.PaymentsDao;
import com.ibm.psd2.api.subscription.dao.SubscriptionDao;
import com.ibm.psd2.commons.beans.ChallengeAnswer;
import com.ibm.psd2.commons.beans.pisp.TxnParty;
import com.ibm.psd2.commons.beans.pisp.TxnRequest;
import com.ibm.psd2.commons.beans.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.beans.subscription.TransactionRequestType;
import com.ibm.psd2.commons.beans.subscription.ViewId;
import com.ibm.psd2.commons.controller.APIController;

@RestController
public class PISPController extends APIController
{
	private static final Logger logger = LogManager.getLogger(PISPController.class);

	@Autowired
	PaymentsDao pdao;

	@Autowired
	SubscriptionDao sdao;

	@Autowired
	KafkaMessageProducer kmp;

	@Value("${version}")
	String version;

	@Value("${kakfa.topic}")
	String topic;

	@Value("${psd2api.integration.useKafka}")
	private boolean useKafka;

	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> createTransactionRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) TxnRequest txnRequest, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			if (txnRequest == null || txnRequest.getTo() == null
					|| (accountId.equals(txnRequest.getTo().getAccount_id()) && bankId.equals(txnRequest.getTo().getBank_id())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			TxnParty payee = new TxnParty(bankId, accountId);
			TxnRequestDetails t = pdao.createTransactionRequest(sib, txnRequest, payee, txnType);

			if (useKafka && t != null && TxnRequestDetails.TXN_STATUS_PENDING.equalsIgnoreCase(t.getStatus()))
			{
				kmp.publishMessage(topic, t.getId(), t.toString());
			}

			response = ResponseEntity.ok(t);
		} catch (Exception ex)
		{
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests/{txnReqId}/challenge", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> answerTransactionChallenge(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@PathVariable("txnReqId") String txnReqId, @RequestBody ChallengeAnswer challengeAnswer, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			TxnRequestDetails tdb = pdao.answerTransactionRequestChallenge(user, viewId, bankId, accountId, txnType,
					txnReqId, challengeAnswer);

			if (useKafka && tdb != null && TxnRequestDetails.TXN_STATUS_PENDING.equalsIgnoreCase(tdb.getStatus()))
			{
				kmp.publishMessage(topic, tdb.getId(), tdb.toString());
			}

			response = ResponseEntity.ok(tdb);
		} catch (Exception ex)
		{
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionRequestType>> getTransactionRequestTypes(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client)
	{
		ResponseEntity<List<TransactionRequestType>> response;
		try
		{
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);

			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			response = ResponseEntity.ok(sib.getTransaction_request_types());
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TxnRequestDetails>> getTransactionRequests(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client)
	{
		ResponseEntity<List<TxnRequestDetails>> response;
		try
		{
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException("Not Subscribed");
			}

			List<TxnRequestDetails> txns = pdao.getTransactionRequests(user, viewId, accountId, bankId);
			response = ResponseEntity.ok(txns);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
