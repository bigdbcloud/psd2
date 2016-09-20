package com.ibm.api.cashew.services;

import java.util.List;

import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

public interface PaymentsService
{
	public List<TransactionRequestType> getTransactionRequestTypes(String appUsername, String bankId, String accountId);
	public TxnRequestDetails createTransactionRequest (String appUsername, String bankId, String accountId, TxnRequest trb);
	public com.ibm.api.cashew.beans.Transaction tagTransaction(String userId, String bankId, String accountId, String txnId, String tag);

}
