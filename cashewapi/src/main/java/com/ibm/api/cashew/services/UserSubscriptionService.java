package com.ibm.api.cashew.services;

import com.ibm.api.cashew.beans.AccountSubscription;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface UserSubscriptionService
{
	public SubscriptionRequest subscribe(AccountSubscription requestedSubscription);
}
