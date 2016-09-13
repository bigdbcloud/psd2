package com.ibm.psd2.commons.subscription.service;

import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;

public interface SubscriptionRequestService
{
	public SubscriptionRequest getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswer cab);
	public SubscriptionRequest createSubscriptionRequest(SubscriptionRequest s);
	public int updateSubscriptionRequestStatus(String id, String status);

}
