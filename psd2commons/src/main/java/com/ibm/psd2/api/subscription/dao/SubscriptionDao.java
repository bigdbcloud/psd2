package com.ibm.psd2.api.subscription.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.subscription.SubscriptionInfoBean;

public interface SubscriptionDao
{
	public SubscriptionInfoBean getSubscriptionInfo(String username, String clientId, String accountId, String bankId)
			throws Exception;

	public List<SubscriptionInfoBean> getSubscriptionInfo(String username, String clientId, String bankId)
			throws Exception;

	public List<SubscriptionInfoBean> getSubscriptionInfo(String username, String clientId) throws Exception;

	public void createSubscriptionInfo(SubscriptionInfoBean s) throws Exception;

}
