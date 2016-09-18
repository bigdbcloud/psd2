package com.ibm.api.cashew.bank.service;

import java.util.List;

import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;

public interface BankService {
	
	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, String string);
	public List<Transaction> getTransactions(String userId, String accountId, String bankId, String fromDate,
			String toDate, String sortBy, Integer offset, Integer limit);

}
