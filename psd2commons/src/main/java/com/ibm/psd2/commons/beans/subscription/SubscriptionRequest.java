package com.ibm.psd2.commons.beans.subscription;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.commons.beans.Challenge;

@JsonInclude(value = Include.NON_EMPTY)
public class SubscriptionRequest implements Serializable
{
	
	public static final String STATUS_INITIATED = "INITIATED";
	public static final String STATUS_SUBSCRIBED = "SUBSCRIBED";
	public static final String STATUS_REJECTED = "REJECTED";
	
	private String id;
	private SubscriptionInfo subscriptionInfo;
	private Date creationDate;
	private Date updatedDate;
	private String status;
	private Challenge challenge;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public SubscriptionInfo getSubscriptionInfo()
	{
		return subscriptionInfo;
	}

	public void setSubscriptionInfo(SubscriptionInfo subscriptionInfo)
	{
		this.subscriptionInfo = subscriptionInfo;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Challenge getChallenge()
	{
		return challenge;
	}

	public void setChallenge(Challenge challenge)
	{
		this.challenge = challenge;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SubscriptionRequest other = (SubscriptionRequest) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public Date getUpdatedDate()
	{
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate)
	{
		this.updatedDate = updatedDate;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

}
