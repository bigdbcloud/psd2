package com.ibm.psd2.api.subscription.service;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.subscription.db.MongoSubscriptionInfoRepository;
import com.ibm.psd2.api.utils.db.MongoConnection;
import com.ibm.psd2.api.utils.db.MongoDocumentParser;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Service
public class SubscriptionServiceImpl implements SubscriptionService
{
	private static final Logger logger = LogManager.getLogger(SubscriptionServiceImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.subscriptions}")
	private String subscriptions;
	
	@Autowired
	MongoSubscriptionInfoRepository msir;

	@Override
	public SubscriptionInfo getSubscriptionInfo(String username, String clientId, String accountId, String bankId)
			throws Exception
	{
		
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", username = " + username);
		return msir.findByUsernameAndClientIdAndAccountIdAndBankId(username, clientId, accountId, bankId);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
//		FindIterable<Document> iterable = coll.find(and(eq("accountId", accountId), eq("bank_id", bankId),
//				eq("username", username), eq("clientId", clientId))).projection(excludeId());
//		SubscriptionInfo s = null;
//
//		Document document = iterable.first();
//		if (document != null)
//		{
//			logger.info("message = " + document.toJson());
//			s = mdp.parse(document, new SubscriptionInfo());
//		}
//		return s;
	}

	@Override
	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId, String bankId)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", username = " + username);
		return msir.findByUsernameAndClientIdAndBankId(username, clientId, bankId);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
//		FindIterable<Document> iterable = coll
//				.find(and(eq("bank_id", bankId), eq("username", username), eq("clientId", clientId)))
//				.projection(excludeId());
//
//		ArrayList<SubscriptionInfo> lst = null;
//		SubscriptionInfo s = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				if (lst == null)
//				{
//					lst = new ArrayList<>();
//				}
//				logger.info("message = " + document.toJson());
//				s = mdp.parse(document, new SubscriptionInfo());
//				lst.add(s);
//			}
//		}
//		return lst;
	}

	@Override
	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId) throws Exception
	{
		logger.info("username = " + username);
		return msir.findByUsernameAndClientId(username, clientId);
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
//		FindIterable<Document> iterable = coll.find(and(eq("username", username), eq("clientId", clientId)))
//				.projection(excludeId());
//
//		ArrayList<SubscriptionInfo> lst = null;
//		SubscriptionInfo s = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				if (lst == null)
//				{
//					lst = new ArrayList<>();
//				}
//				logger.info("message = " + document.toJson());
//				s = mdp.parse(document, new SubscriptionInfo());
//				lst.add(s);
//			}
//		}
//		return lst;
	}

	@Override
	public void createSubscriptionInfo(SubscriptionInfo s) throws Exception
	{
		SubscriptionInfo existingSI = msir.findByUsernameAndClientIdAndAccountIdAndBankId(s.getUsername(), s.getClientId(), s.getAccountId(), s.getBankId());
//		SubscriptionInfo existingSI = getSubscriptionInfo(s.getUsername(), s.getClientId(), s.getAccountId(),
//				s.getBankId());
		
		if (existingSI != null)
		{
			throw new IllegalArgumentException("Subscription Already Exists");
		}
		
		s.setStatus(SubscriptionInfo.STATUS_ACTIVE);
		msir.save(s);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
//		coll.insertOne(mdp.format(s));
	}

}
