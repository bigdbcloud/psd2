package com.ibm.api.cashew.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService
{

	@Override
	public SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest)
	{
		
		RestTemplate template = new RestTemplate();
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
}
