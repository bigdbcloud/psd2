package com.ibm.psd2.api.subscription.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.subscription.SubscriptionInfo;

public interface SubscriptionDao
{
	public SubscriptionInfo getSubscriptionInfo(String username, String clientId, String accountId, String bankId)
			throws Exception;

	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId, String bankId)
			throws Exception;

	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId) throws Exception;

	public void createSubscriptionInfo(SubscriptionInfo s) throws Exception;

}
