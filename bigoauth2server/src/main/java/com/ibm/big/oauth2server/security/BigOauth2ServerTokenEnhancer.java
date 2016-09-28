package com.ibm.big.oauth2server.security;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.big.oauth2server.beans.SocialLoginProvider;
import com.ibm.big.oauth2server.services.SocialLoginProviderService;

@Component
public class BigOauth2ServerTokenEnhancer extends JwtAccessTokenConverter
{
	private static final Logger logger = LogManager.getLogger(BigOauth2ServerTokenEnhancer.class);

	@Autowired
	SocialLoginProviderService slps;

	@Value("${bigoauth2server.provider.name}")
	private String provider;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
	{
		logger.debug("Intance of AccessToken = " + accessToken.getClass().getName());
		logger.debug("Intance of User Authentication = " + authentication.getUserAuthentication().getClass().getName());

		ObjectMapper mapper = new ObjectMapper();

		try
		{
			String strAuth = mapper.writeValueAsString(authentication);
			String strToken = mapper.writeValueAsString(accessToken);
			logger.info("JSON of Authentication = " + strAuth);
			logger.info("JSON of Token = " + strToken);
		}
		catch (JsonProcessingException e)
		{
			logger.error(e.getMessage(), e);
		}

		Map<String, Object> customProps = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());

		logger.info("additional info retrieved: " + accessToken.getAdditionalInformation());

		if (authentication.getUserAuthentication() instanceof OAuth2Authentication)
		{

			OAuth2Authentication userOAuth2Authentication = (OAuth2Authentication) authentication
					.getUserAuthentication();

			Authentication userAuth = userOAuth2Authentication.getUserAuthentication();
			OAuth2Request userOAuth2Request = userOAuth2Authentication.getOAuth2Request();
			Map<String, String> userDetails = (Map<String, String>) userAuth.getDetails();

			logger.info("Setting userId as: " + authentication.getName());
			logger.info("Setting userName as: " + userDetails.get("name"));
			logger.info("Setting clientId as: " + userOAuth2Request.getClientId());

			customProps.put("userId", authentication.getName());
			customProps.put("userName", userDetails.get("name"));
			customProps.put("clientId", userOAuth2Request.getClientId());

			SocialLoginProvider slp = slps.findByClientId(userOAuth2Request.getClientId());
			if (slp != null)
			{
				customProps.put("provider", slp.getProvider());
			}
		}
		else if (authentication.getUserAuthentication() instanceof UsernamePasswordAuthenticationToken)
		{
			UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) authentication
					.getUserAuthentication();
			logger.info("Setting userId & userName as: " + upat.getName());
			logger.info("Setting clientId as: " + authentication.getOAuth2Request().getClientId());

			customProps.put("userName", upat.getName());
			customProps.put("clientId", authentication.getOAuth2Request().getClientId());
			customProps.put("userId", upat.getName());
			customProps.put("provider", provider);
		}

		DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
		customAccessToken.setAdditionalInformation(customProps);
		OAuth2AccessToken retToken = super.enhance(customAccessToken, authentication);

		return retToken;
	}

}
