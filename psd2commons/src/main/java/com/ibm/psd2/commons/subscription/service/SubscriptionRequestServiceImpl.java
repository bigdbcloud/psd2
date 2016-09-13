package com.ibm.psd2.commons.subscription.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.commons.datamodel.Challenge;
import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.commons.subscription.db.MongoSubscriptionRequestRepository;
import com.ibm.psd2.commons.utils.UUIDGenerator;

@Service
public class SubscriptionRequestServiceImpl implements SubscriptionRequestService
{
	private  final Logger logger = LogManager.getLogger(SubscriptionRequestServiceImpl.class);
	
	private static final int CHALLENGE_MAX_ATTEMPTS = 3;

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
		c.setAllowed_attempts(CHALLENGE_MAX_ATTEMPTS);
		
		s.setChallenge(c);
		
		return msrr.save(s);
	}

	@Override
	public int updateSubscriptionRequestStatus(String id, String status)
	{
		return msrr.updateStatus(id, status);
	}

	
}
