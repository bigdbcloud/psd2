package com.ibm.psd2.api.subscription.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.commons.Constants;
import com.ibm.psd2.api.commons.db.MongoConnection;
import com.ibm.psd2.api.commons.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.ChallengeAnswer;
import com.ibm.psd2.commons.beans.Challenge;
import com.ibm.psd2.commons.beans.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.beans.subscription.SubscriptionRequest;
import com.ibm.psd2.commons.utils.UUIDGenerator;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

@Component
public class SubscriptionRequestDaoImpl implements SubscriptionRequestDao
{
	private static final Logger logger = LogManager.getLogger(SubscriptionRequestDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Autowired
	private MongoDocumentParser mdp;

	@Value("${mongodb.collection.subscriptionrequests}")
	private String subscriptionRequests;

	@Override
	public SubscriptionRequest getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswer cab) throws Exception
	{
		logger.info("id = " + id);
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
		FindIterable<Document> iterable = coll.find(
				and(eq("id", id), eq("challenge.id", cab.getId())))
				.projection(excludeId());
		
		SubscriptionRequest s = null;

		Document document = iterable.first();
		if (document != null)
		{
			logger.info("message = " + document.toJson());
			s = mdp.parse(document, new SubscriptionRequest());
		}
		return s;
	}
	
	@Override
	public SubscriptionRequest createSubscriptionRequest(SubscriptionRequest s) throws Exception
	{
		logger.info("Subscription Request = " + s);
		
		s.setId(UUIDGenerator.generateUUID());
		s.setCreationDate(new Date());
		s.setStatus(SubscriptionRequest.STATUS_INITIATED);
		
		Challenge c = new Challenge();
		c.setId(UUIDGenerator.generateUUID());
		c.setChallenge_type("NEW_SUBSCRIPTION");
		c.setAllowed_attempts(Constants.CHALLENGE_MAX_ATTEMPTS);
		
		s.setChallenge(c);
		
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
		coll.insertOne(mdp.format(s));
		
		return s;
	}

	@Override
	public long updateSubscriptionRequestStatus(String id, String status) throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);

		UpdateResult update = coll.updateOne(new Document("id", id),
				new Document("$set", new Document("status", status)).append("$currentDate", new Document("updatedDate", true)));
		return update.getModifiedCount();
	}

	
}
