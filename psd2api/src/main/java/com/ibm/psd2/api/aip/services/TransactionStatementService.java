package com.ibm.psd2.api.aip.services;

import java.util.Date;
import java.util.List;

import com.ibm.psd2.commons.datamodel.aip.Transaction;

public interface TransactionStatementService
{
	public Transaction getTransactionById(String bankId, String accountId, String txnId) throws Exception;

	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection, String fromDate,
			String toDate, String sortBy, Integer page, Integer limit) throws Exception;

}
