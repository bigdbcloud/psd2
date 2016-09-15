package com.ibm.psd2.api.subscription.service;

import java.util.List;

import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;

public interface SubscriptionService
{
	public SubscriptionInfo getSubscriptionInfo(String username, String clientId, String accountId, String bankId);

	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId, String bankId);

	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId);

	public List<SubscriptionInfo> getSubscriptionInfo(String username);

	public void createSubscriptionInfo(SubscriptionInfo s);

}
