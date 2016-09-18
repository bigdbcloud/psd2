package com.ibm.psd2.api.aip.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoTransactionsRepository;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.datamodel.aip.Transaction;

@Service
public class TransactionStatementServiceImpl implements TransactionStatementService
{
	private static final Logger logger = LogManager.getLogger(TransactionStatementServiceImpl.class);

	@Autowired
	MongoTransactionsRepository mtr;

	@Override
	public Transaction getTransactionById(String bankId, String accountId, String txnId)
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", txnId = " + txnId);

		return mtr.findByIdAndThisAccountIdAndThisAccountBankNationalIdentifier(txnId, accountId, bankId);
	}

	@Override
	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection, String fromDate,
			String toDate, String sortBy, Integer page, Integer limit)
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		Date fromDt = null;
		Date toDt = null;

		try
		{
			if (fromDate != null)
			{
				fromDt = Constants.DATE_FORMAT.parse(fromDate);
			}
			if (toDate != null)
			{
				toDt = Constants.DATE_FORMAT.parse(toDate);
			}
		}
		catch (ParseException pe)
		{
			logger.warn("Error Parsing Date: " + pe.getMessage());
		}
		return mtr.getTransactions(bankId, accountId, sortDirection, fromDt, toDt, sortBy, page, limit);
	}

	@Override
	public Transaction createTransaction(Transaction t)
	{
		return mtr.save(t);
	}
	
	@Override
	public Transaction updateTransaction(Transaction t) {
		
		return mtr.save(t);
	}

}
