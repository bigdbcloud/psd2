package com.ibm.big.oauth2server.beans;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document(collection = "users")
@JsonInclude(value = Include.NON_EMPTY)
public class User
{
	@Id
	private String userId;

	private String email;
	private String mobileNumber;
	private String pwd;

	private String authProvider;
	private String authProviderClientId;

	private ArrayList<String> roles;
	private ArrayList<String> aoe;

	private String name;
	private boolean Locked;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAuthProvider()
	{
		return authProvider;
	}

	public void setAuthProvider(String authProvider)
	{
		this.authProvider = authProvider;
	}

	public String getAuthProviderClientId()
	{
		return authProviderClientId;
	}

	public void setAuthProviderClientId(String authProviderClientId)
	{
		this.authProviderClientId = authProviderClientId;
	}

	public String getPwd()
	{
		return pwd;
	}

	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String phone)
	{
		this.mobileNumber = phone;
	}

	public ArrayList<String> getRoles()
	{
		return roles;
	}

	public void setRoles(ArrayList<String> roles)
	{
		this.roles = roles;
	}

	public ArrayList<String> getAoe()
	{
		return aoe;
	}

	public void setAoe(ArrayList<String> aoe)
	{
		this.aoe = aoe;
	}

	public boolean isLocked()
	{
		return Locked;
	}

	public void setLocked(boolean locked)
	{
		Locked = locked;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (email == null)
		{
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		return true;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

}
