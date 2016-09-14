package com.ibm.big.oauth2server.beans;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "clients")
@JsonInclude(value = Include.NON_EMPTY)
public class Client
{
	@Id
	private String clientId;

	private String name;
	private String clientSecret;
	private List<String> grantTypes;
	private List<String> scopes;
	private List<String> authorities;
	private int accessTokenValiditySeconds;
	private int refreshTokenValiditySeconds;

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getClientSecret()
	{
		return clientSecret;
	}

	public void setClientSecret(String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	public List<String> getAuthorities()
	{
		return authorities;
	}

	public void setAuthorities(List<String> authorities)
	{
		this.authorities = authorities;
	}

	public List<String> getGrantTypes()
	{
		return grantTypes;
	}

	public void setGrantTypes(List<String> grantTypes)
	{
		this.grantTypes = grantTypes;
	}

	public List<String> getScopes()
	{
		return scopes;
	}

	public void setScopes(List<String> scopes)
	{
		this.scopes = scopes;
	}

	public int getAccessTokenValiditySeconds()
	{
		return accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds)
	{
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	public int getRefreshTokenValiditySeconds()
	{
		return refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds)
	{
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}
}
