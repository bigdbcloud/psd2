package com.ibm.psd2.commons.subscription.db;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;

public interface MongoSubscriptionInfoRepository extends MongoRepository<SubscriptionInfo, String>
{
	public SubscriptionInfo findByUsernameAndClientIdAndAccountIdAndBankId(String username, String clientId, String accountId, String bankId);
	public List<SubscriptionInfo> findByUsernameAndClientIdAndBankId(String username, String clientId, String bankId);
	public List<SubscriptionInfo> findByUsernameAndClientId(String username, String clientId);
	public List<SubscriptionInfo> findByUsername(String username);
}
