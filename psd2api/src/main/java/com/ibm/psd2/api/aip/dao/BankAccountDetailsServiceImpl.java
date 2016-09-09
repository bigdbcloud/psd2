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
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoBankAccountDetailsRepository;
import com.ibm.psd2.api.common.db.MongoConnection;
import com.ibm.psd2.api.common.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.aip.BankAccountDetailsBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Service
public class BankAccountDetailsServiceImpl implements BankAccountDetailsService
{

	private final Logger logger = LogManager.getLogger(BankAccountDetailsServiceImpl.class);

//	@Autowired
//	private MongoConnection conn;

//	@Autowired
//	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.bankaccounts}")
	private String bankAccounts;
	
	@Autowired
	MongoBankAccountDetailsRepository mgadr;
	
//	private static final String LOG_PREFIX = "document = ";

	@Override
	public BankAccountDetailsBean getBankAccountDetails(String bankId, String accountId) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		return mgadr.findByIdAndBank_id(accountId, bankId);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		FindIterable<Document> iterable = coll.find(and(eq("id", accountId), eq("bank_id", bankId)))
//				.projection(excludeId());
//		BankAccountDetailsBean b = null;
//
//		if (iterable != null)
//		{
//			Document document = iterable.first();
//			if (document != null)
//			{
//				logger.info(LOG_PREFIX + document.toJson());
//				b = mdp.parse(document, new BankAccountDetailsBean());
//			}
//		}
//		return b;
	}

	@Override
	public List<BankAccountDetailsBean> getBankAccounts(String username, String bank_id) throws Exception
	{
		logger.info("bankId = " + bank_id + ", username = " + username);
		return mgadr.findByUsernameAndBank_id(username, bank_id);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		FindIterable<Document> iterable = coll.find(and(eq("username", username), eq("bank_id", bank_id)))
//				.projection(excludeId());
//		ArrayList<BankAccountDetailsBean> accList = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				logger.info(LOG_PREFIX + document.toJson());
//				BankAccountDetailsBean b = mdp.parse(document, new BankAccountDetailsBean());
//				if (accList == null)
//				{
//					accList = new ArrayList<>();
//				}
//				accList.add(b);
//			}
//		}
//
//		return accList;
	}

	@Override
	public BankAccountDetailsBean getBankAccountDetails(String bankId, String accountId, String username)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		
		return mgadr.findByIdAndBank_idAndUsername(accountId, bankId, username);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		FindIterable<Document> iterable = coll
//				.find(and(eq("id", accountId), eq("bank_id", bankId), eq("username", username)))
//				.projection(excludeId());
//		BankAccountDetailsBean b = null;
//
//		if (iterable != null)
//		{
//			Document document = iterable.first();
//			if (document != null)
//			{
//				logger.info(LOG_PREFIX + document.toJson());
//			}
//			b = mdp.parse(document, new BankAccountDetailsBean());
//		}
//
//		return b;
	}

	@Override
	public void createBankAccountDetails(BankAccountDetailsBean b) throws Exception
	{
		mgadr.save(b);
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		coll.insertOne(mdp.format(b));
	}

}
