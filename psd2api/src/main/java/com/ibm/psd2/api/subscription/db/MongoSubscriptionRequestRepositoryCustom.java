package com.ibm.psd2.api.subscription.db;

public interface MongoSubscriptionRequestRepositoryCustom
{
	public int updateStatus(String id, String status);
}
