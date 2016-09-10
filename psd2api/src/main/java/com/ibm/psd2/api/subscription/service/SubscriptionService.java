package com.ibm.psd2.api.subscription.service;

import java.util.List;

import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;

public interface SubscriptionService
{
	public SubscriptionInfo getSubscriptionInfo(String username, String clientId, String accountId, String bankId)
			throws Exception;

	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId, String bankId)
			throws Exception;

	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId) throws Exception;

	public void createSubscriptionInfo(SubscriptionInfo s) throws Exception;

}
