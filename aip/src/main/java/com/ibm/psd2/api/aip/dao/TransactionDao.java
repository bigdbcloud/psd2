package com.ibm.psd2.api.aip.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.aip.TransactionBean;

public interface TransactionDao
{
	public TransactionBean getTransactionById(String bankId, String accountId, String txnId) throws Exception;

	public List<TransactionBean> getTransactions(String bankId, String accountId, String sortDirection, Integer limit, String fromDate,
			String toDate, String sortBy, Integer number) throws Exception;

}
