package com.ibm.api.cashew.services;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface UserAccountService
{
	public SubscriptionRequest subscribe(String username, SubscriptionRequest subscriptionRequest);
	public UserAccount answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca);
	public BankAccountDetailsView getAccountInformation(String appUser, String bankId, String accountId, String viewId);
}
