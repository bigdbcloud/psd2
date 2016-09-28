package com.ibm.big.oauth2server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.big.oauth2server.beans.Client;
import com.ibm.big.oauth2server.db.MongoClientRepository;

@Service
public class ClientServiceImpl implements ClientService
{
	@Autowired
	MongoClientRepository clientRepo;

	@Override
	public Client findById(String clientId)
	{
		return clientRepo.findOne(clientId);
	}

	@Override
	public Client createClient(Client client)
	{

		if (client == null || client.getName() == null)
		{
			throw new IllegalArgumentException("Invalid client passed. Client name is mandatory");
		}

		String clientId = UUID.randomUUID().toString();
		String clientSecret = UUID.randomUUID().toString();
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		clientSecret = bcpe.encode(clientSecret);

		client.setClientId(clientId);
		client.setClientSecret(clientSecret);

		if (client.getGrantTypes() == null)
		{
			List<String> grants = new ArrayList<>();
			grants.add("authorization_code");
			client.setGrantTypes(grants);
		}

		if (client.getAccessTokenValiditySeconds() == 0)
		{
			client.setAccessTokenValiditySeconds(3600);
		}

		if (client.getRefreshTokenValiditySeconds() == 0)
		{
			client.setRefreshTokenValiditySeconds(86400);
		}

		if (client.getAuthorities() == null)
		{
			List<String> authorities = new ArrayList<>();
			authorities.add("ROLE_USER");
			client.setAuthorities(authorities);
		}

		if (client.getScopes() == null)
		{
			List<String> scopes = new ArrayList<>();
			scopes.add("read");
			client.setScopes(scopes);
		}
		return clientRepo.save(client);
	}

}
