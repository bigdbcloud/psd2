package com.ibm.psd2.api.subscription.service;

import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface SubscriptionRequestService
{
	public SubscriptionRequest getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswer cab);
	public SubscriptionRequest createSubscriptionRequest(SubscriptionRequest s);
	public int updateSubscriptionRequestStatus(String id, String status);
	public SubscriptionInfo validateTxnChallengeAnswer(String id, ChallengeAnswer cab);

}
