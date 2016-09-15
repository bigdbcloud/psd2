package com.ibm.api.cashew.security;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

public class CashewTokenEnhancer extends JwtAccessTokenConverter
{
	private static final Logger logger = LogManager.getLogger(CashewTokenEnhancer.class);

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
	{
		logger.debug("Intance of AccessToken = " + accessToken.getClass().getName());

		Map<String, Object> customProps = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

		customProps.put("username", authentication.getName());
		customProps.put("userDetails", authentication.getDetails());
		customProps.put("provider", authentication.getOAuth2Request().getClientId());

		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);

		customAccessToken.setAdditionalInformation(customProps);

		return super.enhance(customAccessToken, authentication);
	}

}
