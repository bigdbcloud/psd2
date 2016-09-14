package com.ibm.api.oauth2server.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ibm.api.oauth2server.beans.ClientInfo;
import com.ibm.api.oauth2server.db.MongoClientRepository;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService
{
	private final Logger logger = LogManager.getLogger(ClientDetailsServiceImpl.class);
	
	@Autowired
	private MongoClientRepository mcr;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException
	{
		ClientInfo clientInfo = null;

		logger.debug("Searching client = " + clientId);
		try
		{
			clientInfo = getClientInfo(clientId);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ClientRegistrationException(e.getMessage(), e);
		}
		
		if (clientInfo == null)
		{
			throw new ClientRegistrationException("Client Not Found");
		}
		
		if (clientInfo.isLocked())
		{
			throw new ClientRegistrationException("Client is locked");
		}

		BaseClientDetails bcd = new BaseClientDetails();
		bcd.setClientId(clientInfo.getClientId());
		bcd.setClientSecret(clientInfo.getClientSecret());
		bcd.setAccessTokenValiditySeconds(clientInfo.getAccessTokenValidity());
		bcd.setRefreshTokenValiditySeconds(clientInfo.getRefreshTokenValidity());
		bcd.setAuthorizedGrantTypes(StringUtils.commaDelimitedListToSet(clientInfo.getGrantTypes()));
		bcd.setScope(StringUtils.commaDelimitedListToSet(clientInfo.getScope()));
		bcd.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(clientInfo.getAuthorities()));
		return bcd;
	}
	
	private ClientInfo getClientInfo(String clientId) throws Exception
	{
		return mcr.findOne(clientId);
	}	
	
	public void createClient(ClientInfo c) throws Exception
	{
		c.setAccessTokenValidity(86400);
		c.setRefreshTokenValidity(432000);
		if (mcr.findOne(c.getClientId()) != null)
		{
			throw new IllegalArgumentException("Client already exists");
		}
		mcr.save(c);
	}

}
