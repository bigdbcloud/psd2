package com.ibm.psd2.api.subscription.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.subscription.db.MongoSubscriptionRequestRepository;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.commons.datamodel.Challenge;
import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.commons.utils.UUIDGenerator;

@Service
public class SubscriptionRequestServiceImpl implements SubscriptionRequestService
{
	private static final Logger logger = LogManager.getLogger(SubscriptionRequestServiceImpl.class);

//	@Autowired
//	private MongoConnection conn;
//
//	@Autowired
//	private MongoDocumentParser mdp;
//
//	@Value("${mongodb.collection.subscriptionrequests}")
//	private String subscriptionRequests;
	
	@Autowired
	private MongoSubscriptionRequestRepository msrr;

	@Override
	public SubscriptionRequest getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswer cab)
	{
		logger.info("id = " + id);
		
		if (cab == null)
		{
			throw new IllegalArgumentException("Invalid ChallengeAnswer. It can't be null");
		}
		
		return msrr.findByIdAndChallengeId(id, cab.getId());
		
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
//		FindIterable<Document> iterable = coll.find(
//				and(eq("id", id), eq("challenge.id", cab.getId())))
//				.projection(excludeId());
//		
//		SubscriptionRequest s = null;
//
//		Document document = iterable.first();
//		if (document != null)
//		{
//			logger.info("message = " + document.toJson());
//			s = mdp.parse(document, new SubscriptionRequest());
//		}
//		return s;
	}
	
	@Override
	public SubscriptionRequest createSubscriptionRequest(SubscriptionRequest s)
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
		
		return msrr.save(s);
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
//		coll.insertOne(mdp.format(s));
//		return s;
	}

	@Override
	public int updateSubscriptionRequestStatus(String id, String status)
	{
		return msrr.updateStatus(id, status);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(subscriptionRequests);
//
//		UpdateResult update = coll.updateOne(new Document("id", id),
//				new Document("$set", new Document("status", status)).append("$currentDate", new Document("updatedDate", true)));
//		return update.getModifiedCount();
	}

	
}
