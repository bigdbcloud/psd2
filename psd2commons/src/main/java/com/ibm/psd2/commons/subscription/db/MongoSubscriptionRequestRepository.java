package com.ibm.psd2.commons.subscription.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;

public interface MongoSubscriptionRequestRepository extends MongoRepository<SubscriptionRequest, String>, MongoSubscriptionRequestRepositoryCustom
{
	public SubscriptionRequest findByIdAndChallengeId(String id, String challengeId);
}
