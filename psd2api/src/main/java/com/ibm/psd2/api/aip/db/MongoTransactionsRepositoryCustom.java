package com.ibm.psd2.api.aip.db;

import java.util.Date;
import java.util.List;

import com.ibm.psd2.datamodel.aip.Transaction;

public interface MongoTransactionsRepositoryCustom
{
	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection, Date fromDate,
			Date toDate, String sortBy, Integer page, Integer limit);
}
