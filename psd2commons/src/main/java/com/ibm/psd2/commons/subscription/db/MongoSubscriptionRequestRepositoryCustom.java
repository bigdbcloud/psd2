package com.ibm.psd2.commons.subscription.db;

public interface MongoSubscriptionRequestRepositoryCustom
{
	public int updateStatus(String id, String status);
}
