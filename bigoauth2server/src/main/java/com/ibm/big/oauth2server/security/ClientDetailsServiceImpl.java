package com.ibm.big.oauth2server.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import com.ibm.big.oauth2server.beans.Client;
import com.ibm.big.oauth2server.services.ClientService;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService
{
	private final Logger logger = LogManager.getLogger(ClientDetailsServiceImpl.class);

	@Autowired
	ClientService clientService;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException
	{

		logger.debug("loading client details for clientId = " + clientId);
		Client client = null;

		try
		{
			client = clientService.findById(clientId);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ClientRegistrationException(e.getMessage(), e);
		}

		if (client == null)
		{
			throw new ClientRegistrationException("Client Not Found");
		}

		BaseClientDetails bcd = new BaseClientDetails();
		bcd.setClientId(client.getClientId());
		bcd.setClientSecret(client.getClientSecret());
		bcd.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		bcd.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
		bcd.setAuthorizedGrantTypes(client.getGrantTypes());
		bcd.setScope(client.getScopes());
		if (client.getAuthorities() != null && !client.getAuthorities().isEmpty())
		{
			String[] authorities = new String[client.getAuthorities().size()];
			authorities = client.getAuthorities().toArray(authorities);
			bcd.setAuthorities(AuthorityUtils.createAuthorityList(authorities));
		}
		else
		{
			bcd.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		}
		return bcd;
	}

}
