package com.ibm.api.cashew.services;

import org.springframework.stereotype.Service;

import com.ibm.api.cashew.beans.AccountSubscription;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService
{

	@Override
	public SubscriptionRequest subscribe(AccountSubscription requestedSubscription)
	{
		SubscriptionRequest sr = new SubscriptionRequest();
		SubscriptionInfo si = new SubscriptionInfo();
		
		si.setAccountId(requestedSubscription.getAccount().getId());
		si.setBankId(requestedSubscription.getAccount().getBankId());
		si.setClientId("cashew");
		si.setUsername(requestedSubscription.getAccount().getUsername());
		
		
		sr.setSubscriptionInfo(si);
		// TODO Auto-generated method stub
		return null;
	}
	
}
