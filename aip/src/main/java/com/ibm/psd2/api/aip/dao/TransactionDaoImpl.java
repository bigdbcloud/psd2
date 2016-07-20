package com.ibm.psd2.api.aip.dao;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.commons.Constants;
import com.ibm.psd2.api.commons.db.MongoConnection;
import com.ibm.psd2.api.commons.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.aip.Transaction;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class TransactionDaoImpl implements TransactionDao
{
	private static final Logger logger = LogManager.getLogger(TransactionDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.transactions}")
	private String transactions;


	public Transaction getTransactionById(String bankId, String accountId, String txnId) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", txnId = " + txnId);
		MongoCollection<Document> coll = conn.getDB().getCollection(transactions);
		
		FindIterable<Document> iterable = coll.find(and(eq("id", txnId), eq("this_account.id", accountId), eq("this_account.bank.national_identifier", bankId)))
				.projection(excludeId());
		Transaction t = null;

		if (iterable != null)
		{
			Document document = iterable.first();
			if (document != null)
			{
				logger.info("Transaction = " + document.toJson());
				t = mdp.parse(document, new Transaction());
			}
		}

		return t;
	}

	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection,Integer limit, String fromDate, String toDate, String sortBy, Integer number) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		MongoCollection<Document> coll = conn.getDB().getCollection(transactions);
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.TXN_DATE_FORMAT);
		Date fDate = null;
		Date tDate = null;
		
		List<Bson> criteria = new ArrayList<>();
		
		criteria.add(eq("this_account.id", accountId));
		criteria.add(eq("this_account.bank.national_identifier", bankId));

		if (fromDate != null)
		{
			fDate = sdf.parse(fromDate);
			criteria.add(gte("details.posted", fDate));
		}
		
		if (toDate != null)
		{
			tDate = sdf.parse(toDate);
			criteria.add(lte("details.posted", tDate));
		}

		Bson sortDirect = null;
		
		if (sortDirection != null && !sortDirection.isEmpty() && Constants.SORT_ASCENDING.equalsIgnoreCase(sortDirection))
		{
			sortDirect = ascending("details.posted");
		}
		else
		{
			sortDirect = descending("details.posted");
		}
		
		int docLimit = 0;
		if (limit != null)
		{
			docLimit = limit;
		}
		
		FindIterable<Document> iterable = coll.find(and(criteria)).sort(sortDirect).limit(docLimit).projection(excludeId());
		
		List<Transaction> lst = null;
		for (Document document : iterable)
		{
			if (document != null)
			{
				if (lst == null)
				{
					lst = new ArrayList<>();
				}
				logger.info("Transaction = " + document.toJson());
				Transaction t = mdp.parse(document, new Transaction());
				lst.add(t);
			}
		}

		return lst;
	}




}
