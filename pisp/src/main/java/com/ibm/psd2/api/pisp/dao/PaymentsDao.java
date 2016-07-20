package com.ibm.psd2.api.pisp.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.ChallengeAnswerBean;
import com.ibm.psd2.commons.beans.pisp.TxnPartyBean;
import com.ibm.psd2.commons.beans.pisp.TxnRequestBean;
import com.ibm.psd2.commons.beans.pisp.TxnRequestDetailsBean;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfoBean;

public interface PaymentsDao
{
	public TxnRequestDetailsBean createTransactionRequest(SubscriptionInfoBean sib, TxnRequestBean trb,
			TxnPartyBean payee, String txnType) throws Exception;

	public List<TxnRequestDetailsBean> getTransactionRequests(String username, String viewId, String accountId,
			String bankId) throws Exception;

	public TxnRequestDetailsBean answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswerBean t) throws Exception;

}
