package com.ibm.api.oauth2server.beans;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "Clients")
@JsonInclude(value = Include.NON_EMPTY)
public class ClientInfo implements Serializable
{
	@Id
	String clientId;
	String clientSecret;
	String scope;
	String grantTypes;
	int accessTokenValidity;
	int refreshTokenValidity;
	String clientName;
	String redirectUris;
	String authorities;
	boolean locked;

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public String getClientSecret()
	{
		return clientSecret;
	}

	public void setClientSecret(String clientSecret)
	{
		this.clientSecret = clientSecret;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public String getGrantTypes()
	{
		return grantTypes;
	}

	public void setGrantTypes(String grantTypes)
	{
		this.grantTypes = grantTypes;
	}

	public int getAccessTokenValidity()
	{
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(int accessTokenValidity)
	{
		this.accessTokenValidity = accessTokenValidity;
	}

	public int getRefreshTokenValidity()
	{
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(int refreshTokenValidity)
	{
		this.refreshTokenValidity = refreshTokenValidity;
	}

	public String getClientName()
	{
		return clientName;
	}

	public void setClientName(String clientName)
	{
		this.clientName = clientName;
	}

	public String getAuthorities()
	{
		return authorities;
	}

	public void setAuthorities(String authorities)
	{
		this.authorities = authorities;
	}

	public String getRedirectUris()
	{
		return redirectUris;
	}

	public void setRedirectUris(String redirectUris)
	{
		this.redirectUris = redirectUris;
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientInfo other = (ClientInfo) obj;
		if (clientId == null)
		{
			if (other.clientId != null)
				return false;
		}
		else if (!clientId.equals(other.clientId))
			return false;
		return true;
	}
}
