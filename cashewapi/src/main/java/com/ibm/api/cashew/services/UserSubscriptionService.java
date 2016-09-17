package com.ibm.api.cashew.services;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface UserSubscriptionService
{
	public SubscriptionRequest subscribe(UserAccount userAccount);
	public UserAccount answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca);
}
