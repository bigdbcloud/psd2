package com.ibm.psd2.api.security;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "ApiClients")
@JsonInclude(value = Include.NON_EMPTY)
public class APIClient implements Serializable
{
	@Id
	String clientname;
	String password;
	boolean locked;

	public String getClientname()
	{
		return clientname;
	}

	public void setClientname(String clientname)
	{
		this.clientname = clientname;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}

}
