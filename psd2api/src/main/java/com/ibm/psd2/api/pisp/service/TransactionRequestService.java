package com.ibm.psd2.api.pisp.service;

import java.util.List;

import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.pisp.TxnParty;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequest;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;

public interface TransactionRequestService
{
	public TxnRequestDetails createTransactionRequest(SubscriptionInfo sib, TxnRequest trb,
			TxnParty payee, String txnType) throws Exception;

	public List<TxnRequestDetails> getTransactionRequests(String username, String viewId, String accountId,
			String bankId) throws Exception;

	public TxnRequestDetails answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswer t) throws Exception;

}
