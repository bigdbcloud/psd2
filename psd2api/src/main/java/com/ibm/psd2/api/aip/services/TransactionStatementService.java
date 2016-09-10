package com.ibm.psd2.api.aip.services;

import java.util.Date;
import java.util.List;

import com.ibm.psd2.commons.datamodel.aip.TransactionBean;

public interface TransactionStatementService
{
	public TransactionBean getTransactionById(String bankId, String accountId, String txnId) throws Exception;

	public List<TransactionBean> getTransactions(String bankId, String accountId, String sortDirection, String fromDate,
			String toDate, String sortBy, Integer page, Integer limit) throws Exception;

}
