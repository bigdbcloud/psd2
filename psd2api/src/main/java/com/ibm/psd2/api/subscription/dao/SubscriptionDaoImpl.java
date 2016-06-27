package com.ibm.psd2.api.subscription.dao;

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
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.common.db.MongoConnection;
import com.ibm.psd2.api.common.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfoBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class SubscriptionDaoImpl implements SubscriptionDao
{
	private static final Logger logger = LogManager.getLogger(SubscriptionDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.subscriptions}")
	private String subscriptions;

	@Override
	public SubscriptionInfoBean getSubscriptionInfo(String username, String clientId, String accountId, String bankId)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", username = " + username);
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
		FindIterable<Document> iterable = coll.find(and(eq("accountId", accountId), eq("bank_id", bankId),
				eq("username", username), eq("clientId", clientId))).projection(excludeId());
		SubscriptionInfoBean s = null;

		Document document = iterable.first();
		if (document != null)
		{
			logger.info("message = " + document.toJson());
			s = mdp.parse(document, new SubscriptionInfoBean());
		}
		return s;
	}

	@Override
	public List<SubscriptionInfoBean> getSubscriptionInfo(String username, String clientId, String bankId)
			throws Exception
	{
		logger.info("bankId = " + bankId + ", username = " + username);
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
		FindIterable<Document> iterable = coll
				.find(and(eq("bank_id", bankId), eq("username", username), eq("clientId", clientId)))
				.projection(excludeId());

		ArrayList<SubscriptionInfoBean> lst = null;
		SubscriptionInfoBean s = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				if (lst == null)
				{
					lst = new ArrayList<>();
				}
				logger.info("message = " + document.toJson());
				s = mdp.parse(document, new SubscriptionInfoBean());
				lst.add(s);
			}
		}
		return lst;
	}

	@Override
	public List<SubscriptionInfoBean> getSubscriptionInfo(String username, String clientId) throws Exception
	{
		logger.info("username = " + username);
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
		FindIterable<Document> iterable = coll.find(and(eq("username", username), eq("clientId", clientId)))
				.projection(excludeId());

		ArrayList<SubscriptionInfoBean> lst = null;
		SubscriptionInfoBean s = null;

		for (Document document : iterable)
		{
			if (document != null)
			{
				if (lst == null)
				{
					lst = new ArrayList<>();
				}
				logger.info("message = " + document.toJson());
				s = mdp.parse(document, new SubscriptionInfoBean());
				lst.add(s);
			}
		}
		return lst;
	}

	@Override
	public void createSubscriptionInfo(SubscriptionInfoBean s) throws Exception
	{
		SubscriptionInfoBean existingSI = getSubscriptionInfo(s.getUsername(), s.getClientId(), s.getAccountId(),
				s.getBank_id());
		
		if (existingSI != null)
		{
			throw new IllegalArgumentException("Subscription Already Exists");
		}

		s.setStatus(SubscriptionInfoBean.STATUS_ACTIVE);
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptions);
		coll.insertOne(mdp.format(s));
	}

}
