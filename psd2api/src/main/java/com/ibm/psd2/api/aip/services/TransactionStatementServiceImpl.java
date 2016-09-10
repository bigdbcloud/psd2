package com.ibm.psd2.api.aip.services;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoTransactionsRepository;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.commons.datamodel.aip.Transaction;

@Service
public class TransactionStatementServiceImpl implements TransactionStatementService
{
	private static final Logger logger = LogManager.getLogger(TransactionStatementServiceImpl.class);

	// @Autowired
	// private MongoConnection conn;
	//
	// @Autowired
	// private MongoDocumentParser mdp;
	//
	// @Value("${mongodb.collection.transactions}")
	// private String transactions;

	@Autowired
	MongoTransactionsRepository mtr;

	public Transaction getTransactionById(String bankId, String accountId, String txnId) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", txnId = " + txnId);

		return mtr.findByIdAndThisAccountIdAndThisAccountBankNationalIdentifier(txnId, accountId, bankId);

		// MongoCollection<Document> coll =
		// conn.getDB().getCollection(transactions);
		//
		// FindIterable<Document> iterable = coll.find(and(eq("id", txnId),
		// eq("this_account.id", accountId),
		// eq("this_account.bank.national_identifier",
		// bankId))).projection(excludeId());
		// Transaction t = null;
		//
		// if (iterable != null)
		// {
		// Document document = iterable.first();
		// if (document != null)
		// {
		// logger.info("Transaction = " + document.toJson());
		// t = mdp.parse(document, new Transaction());
		// }
		// }
		//
		// return t;
	}

	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection, String fromDate,
			String toDate, String sortBy, Integer page, Integer limit) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		Date fromDt = null;
		Date toDt = null;

		if (fromDate != null)
		{
			fromDt = Constants.DATE_FORMAT.parse(fromDate);
		}
		if (toDate != null)
		{
			toDt = Constants.DATE_FORMAT.parse(toDate);
		}
		;
		return mtr.getTransactions(bankId, accountId, sortDirection, fromDt, toDt, sortBy, page, limit);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(transactions);
//
//		SimpleDateFormat sdf = new SimpleDateFormat(Constants.TXN_DATE_FORMAT);
//		Date fDate = null;
//		Date tDate = null;
//
//		List<Bson> criteria = new ArrayList<>();
//
//		criteria.add(eq("this_account.id", accountId));
//		criteria.add(eq("this_account.bank.national_identifier", bankId));
//
//		if (fromDate != null)
//		{
//			fDate = sdf.parse(fromDate);
//			criteria.add(gte("details.posted", fDate));
//		}
//
//		if (toDate != null)
//		{
//			tDate = sdf.parse(toDate);
//			criteria.add(lte("details.posted", tDate));
//		}
//
//		Bson sortDirect = null;
//
//		if (sortDirection != null && !sortDirection.isEmpty()
//				&& Constants.SORT_ASCENDING.equalsIgnoreCase(sortDirection))
//		{
//			sortDirect = ascending("details.posted");
//		}
//		else
//		{
//			sortDirect = descending("details.posted");
//		}
//
//		int docLimit = 0;
//		if (limit != null)
//		{
//			docLimit = limit;
//		}
//
//		FindIterable<Document> iterable = coll.find(and(criteria)).sort(sortDirect).limit(docLimit)
//				.projection(excludeId());
//
//		List<Transaction> lst = null;
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				if (lst == null)
//				{
//					lst = new ArrayList<>();
//				}
//				logger.info("Transaction = " + document.toJson());
//				Transaction t = mdp.parse(document, new Transaction());
//				lst.add(t);
//			}
//		}
//
//		return lst;
	}

}
