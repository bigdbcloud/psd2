package com.ibm.psd2.api.aip.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoBankAccountDetailsRepository;
import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;

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
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId) throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		return mgadr.findByIdAndBankId(accountId, bankId);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		FindIterable<Document> iterable = coll.find(and(eq("id", accountId), eq("bank_id", bankId)))
//				.projection(excludeId());
//		BankAccountDetails b = null;
//
//		if (iterable != null)
//		{
//			Document document = iterable.first();
//			if (document != null)
//			{
//				logger.info(LOG_PREFIX + document.toJson());
//				b = mdp.parse(document, new BankAccountDetails());
//			}
//		}
//		return b;
	}

	@Override
	public List<BankAccountDetails> getBankAccounts(String username, String bank_id) throws Exception
	{
		logger.info("bankId = " + bank_id + ", username = " + username);
		return mgadr.findByUsernameAndBankId(username, bank_id);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		FindIterable<Document> iterable = coll.find(and(eq("username", username), eq("bank_id", bank_id)))
//				.projection(excludeId());
//		ArrayList<BankAccountDetails> accList = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				logger.info(LOG_PREFIX + document.toJson());
//				BankAccountDetails b = mdp.parse(document, new BankAccountDetails());
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
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId, String username)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		
		return mgadr.findByIdAndBankIdAndUsername(accountId, bankId, username);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		FindIterable<Document> iterable = coll
//				.find(and(eq("id", accountId), eq("bank_id", bankId), eq("username", username)))
//				.projection(excludeId());
//		BankAccountDetails b = null;
//
//		if (iterable != null)
//		{
//			Document document = iterable.first();
//			if (document != null)
//			{
//				logger.info(LOG_PREFIX + document.toJson());
//			}
//			b = mdp.parse(document, new BankAccountDetails());
//		}
//
//		return b;
	}

	@Override
	public void createBankAccountDetails(BankAccountDetails b) throws Exception
	{
		mgadr.save(b);
//		MongoCollection<Document> coll = conn.getDB().getCollection(bankAccounts);
//		coll.insertOne(mdp.format(b));
	}

}
