package com.ibm.psd2.api.pisp.service;

import java.text.ParseException;
import java.util.List;

import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;

public interface TransactionRequestService
{
	public TxnRequestDetails createTransactionRequest(SubscriptionInfo sib, TxnRequest trb, TxnParty payee,
			String txnType) throws ParseException;

	public List<TxnRequestDetails> getTransactionRequests(String username, String viewId, String accountId,
			String bankId);

	public TxnRequestDetails answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswer t) throws ParseException;
	
	public TxnRequestDetails createTransactionRequestHack(SubscriptionInfo sib, TxnRequest trb, TxnParty payee,
			String txnType, String postedDate);
}
