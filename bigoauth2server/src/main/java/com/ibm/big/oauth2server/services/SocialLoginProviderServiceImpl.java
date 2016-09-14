package com.ibm.big.oauth2server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.big.oauth2server.beans.SocialLoginProvider;
import com.ibm.big.oauth2server.db.MongoSocialLoginProviderRepository;

@Service
public class SocialLoginProviderServiceImpl implements SocialLoginProviderService
{

	@Autowired
	MongoSocialLoginProviderRepository mslpr;

	@Override
	public SocialLoginProvider findByClientId(String clientId)
	{
		return mslpr.findOne(clientId);
	}

	@Override
	public SocialLoginProvider save(SocialLoginProvider provider)
	{
		if (provider == null || provider.getClientId() == null || provider.getClientId().isEmpty()
				|| provider.getProvider() == null || provider.getProvider().isEmpty())
		{
			throw new IllegalArgumentException(
					"clientId & provider attributes of class SocialLoginProvider are mandatory");
		}
		return mslpr.save(provider);
	}

}
