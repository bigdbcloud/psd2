package com.ibm.psd2.api.pisp.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.ChallengeAnswer;
import com.ibm.psd2.commons.beans.pisp.TxnParty;
import com.ibm.psd2.commons.beans.pisp.TxnRequest;
import com.ibm.psd2.commons.beans.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfo;

public interface PaymentsDao
{
	public TxnRequestDetails createTransactionRequest(SubscriptionInfo sib, TxnRequest trb,
			TxnParty payee, String txnType) throws Exception;

	public List<TxnRequestDetails> getTransactionRequests(String username, String viewId, String accountId,
			String bankId) throws Exception;

	public TxnRequestDetails answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswer t) throws Exception;

}
