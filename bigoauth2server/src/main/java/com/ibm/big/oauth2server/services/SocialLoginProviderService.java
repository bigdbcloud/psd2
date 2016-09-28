package com.ibm.big.oauth2server.services;

import com.ibm.big.oauth2server.beans.SocialLoginProvider;

public interface SocialLoginProviderService
{
	SocialLoginProvider findByClientId(String clientId);

	SocialLoginProvider save(SocialLoginProvider provider);
}
