package com.ibm.psd2.api.aip.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.aip.Transaction;

public interface TransactionDao
{
	public Transaction getTransactionById(String bankId, String accountId, String txnId) throws Exception;

	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection, Integer limit, String fromDate,
			String toDate, String sortBy, Integer number) throws Exception;

}
