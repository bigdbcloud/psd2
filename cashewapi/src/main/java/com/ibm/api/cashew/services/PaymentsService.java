package com.ibm.api.cashew.services;

import java.util.List;

import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.pisp.CounterParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

public interface PaymentsService
{
	public List<TransactionRequestType> getTransactionRequestTypes(String appUsername, String bankId, String accountId);

	public TxnRequestDetails createTransactionRequest(String appUsername, String bankId, String accountId,
			TxnRequest trb);

	public com.ibm.api.cashew.beans.Transaction tagTransaction(String userId, String bankId, String accountId,
			String txnId, String tag);

	public List<CounterParty> getPayees(String appUsername, String bankId, String accountId) throws Exception;

	public TxnRequestDetails answerTxnChallnge(String userId, String bankId, String accountId, String txnReqType,
			String txnId, ChallengeAnswer ca);

}
