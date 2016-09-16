package com.ibm.api.cashew.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService
{
	@Autowired
	RestTemplate restTemplate;

	@Override
	public SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest)
	{
		SubscriptionRequest res = null;
		try
		{
			URI uri = new URI("http://172.30.91.54:8082/psd2api/subscriptionRequest");
			RequestEntity<SubscriptionRequest> re = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", "Basic Y2FzaGV3OnBhc3N3b3JkMDE=").body(subscriptionRequest);
			ResponseEntity<SubscriptionRequest> response = restTemplate.exchange(re, SubscriptionRequest.class);
			res = response.getBody();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		return res;
	}

}
