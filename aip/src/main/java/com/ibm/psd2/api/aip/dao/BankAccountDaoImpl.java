package com.ibm.psd2.api.aip.dao;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.commons.db.MongoConnection;
import com.ibm.psd2.api.commons.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.aip.BankAccountDetails;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class BankAccountDaoImpl implements BankAccountDao
{

	private static final Logger logger = LogManager.getLogger(BankAccountDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.bankaccounts}")
	private String bankAccounts;
	
	private static final String LOG_PREFIX = "document = ";

	@Override
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		FindIterable<Document> iterable = coll.find(and(eq("id", accountId), eq("bank_id", bankId)))
				.projection(excludeId());
		BankAccountDetails b = null;

		if (iterable != null)
		{
			Document document = iterable.first();
			if (document != null)
			{
				logger.info(LOG_PREFIX + document.toJson());
				b = mdp.parse(document, new BankAccountDetails());
			}
		}

		return b;
	}

	@Override
	public List<BankAccountDetails> getBankAccounts(String username, String bank_id) throws Exception
	{
		logger.info("bankId = " + bank_id + ", username = " + username);
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		FindIterable<Document> iterable = coll.find(and(eq("username", username), eq("bank_id", bank_id)))
				.projection(excludeId());
		ArrayList<BankAccountDetails> accList = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				logger.info(LOG_PREFIX + document.toJson());
				BankAccountDetails b = mdp.parse(document, new BankAccountDetails());
				if (accList == null)
				{
					accList = new ArrayList<>();
				}
				accList.add(b);
			}
		}

		return accList;
	}

	@Override
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId, String username)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		FindIterable<Document> iterable = coll
				.find(and(eq("id", accountId), eq("bank_id", bankId), eq("username", username)))
				.projection(excludeId());
		BankAccountDetails b = null;

		if (iterable != null)
		{
			Document document = iterable.first();
			if (document != null)
			{
				logger.info(LOG_PREFIX + document.toJson());
			}
			b = mdp.parse(document, new BankAccountDetails());
		}

		return b;
	}

	@Override
	public void createBankAccountDetails(BankAccountDetails b) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
		coll.insertOne(mdp.format(b));
	}

}
