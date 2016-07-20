package com.ibm.psd2.api.subscription.dao;

import com.ibm.psd2.commons.beans.ChallengeAnswer;
import com.ibm.psd2.commons.beans.subscription.SubscriptionRequest;

public interface SubscriptionRequestDao
{
	public SubscriptionRequest getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswer cab) throws Exception;
	public SubscriptionRequest createSubscriptionRequest(SubscriptionRequest s) throws Exception;
	public long updateSubscriptionRequestStatus(String id, String status) throws Exception;

}
