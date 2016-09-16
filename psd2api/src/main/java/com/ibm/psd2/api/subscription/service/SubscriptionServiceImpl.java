package com.ibm.psd2.api.subscription.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.subscription.db.MongoSubscriptionInfoRepository;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;

@Service
public class SubscriptionServiceImpl implements SubscriptionService
{
	private static final Logger logger = LogManager.getLogger(SubscriptionServiceImpl.class);

	@Autowired
	MongoSubscriptionInfoRepository msir;

	@Override
	public SubscriptionInfo getSubscriptionInfo(String username, String clientId, String accountId, String bankId)
	{
		
		logger.info("bankId = " + bankId + ", accountId = " + accountId + ", username = " + username + ", clientId = " + clientId);
		return msir.findByUsernameAndClientIdAndAccountIdAndBankId(username, clientId, accountId, bankId);
	}

	@Override
	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId, String bankId)
	{
		logger.info("bankId = " + bankId + ", username = " + username + " , clientId = " + clientId);
		List<SubscriptionInfo> lst = msir.findByUsernameAndClientIdAndBankId(username, clientId, bankId);
		logger.debug("subscriptions = " + lst);
		return lst;
	}

	@Override
	public List<SubscriptionInfo> getSubscriptionInfo(String username, String clientId)
	{
		logger.info("username = " + username + ", clientId = " + clientId);
		return msir.findByUsernameAndClientId(username, clientId);
	}

	@Override
	public void createSubscriptionInfo(SubscriptionInfo s)
	{
		SubscriptionInfo existingSI = msir.findByUsernameAndClientIdAndAccountIdAndBankId(s.getUsername(), s.getClientId(), s.getAccountId(), s.getBankId());
		if (existingSI != null)
		{
			throw new IllegalArgumentException("Subscription Already Exists for account: " + s.getAccountId());
		}
		
		s.setStatus(SubscriptionInfo.STATUS_ACTIVE);
		msir.save(s);
	}

	@Override
	public List<SubscriptionInfo> getSubscriptionInfo(String username)
	{
		logger.info("username = " + username);
		return msir.findByUsername(username);
	}

	
}
