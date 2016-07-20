package com.ibm.psd2.commons.beans.subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_EMPTY)
public class SubscriptionInfo implements Serializable
{
	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_LOCKED = "LOCKED";

	private String username;
	private String accountId;
	private String bank_id;
	private String clientId;
	private ArrayList<ViewId> viewIds;
	private String status;

	private List<TransactionRequestType> transaction_request_types;
	private List<TransactionLimit> limits;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public List<TransactionRequestType> getTransaction_request_types()
	{
		return transaction_request_types;
	}

	public void setTransaction_request_types(List<TransactionRequestType> transaction_request_types)
	{
		this.transaction_request_types = transaction_request_types;
	}

	public List<TransactionLimit> getLimits()
	{
		return limits;
	}

	public void setLimits(List<TransactionLimit> limits)
	{
		this.limits = limits;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getBank_id()
	{
		return bank_id;
	}

	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}

	public ArrayList<ViewId> getViewIds()
	{
		return viewIds;
	}

	public void setViewIds(ArrayList<ViewId> viewIds)
	{
		this.viewIds = viewIds;
	}

	public void addViewIds(ViewId viewId)
	{
		if (this.viewIds == null)
		{
			this.viewIds = new ArrayList<>();
		}

		this.viewIds.add(viewId);
	}

	public void addTransaction_request_types(TransactionRequestType b)
	{
		if (transaction_request_types == null)
		{
			transaction_request_types = new ArrayList<>();
		}
		transaction_request_types.add(b);
	}

	public void addLimits(TransactionLimit ab)
	{
		if (limits == null)
		{
			limits = new ArrayList<>();
		}

		limits.add(ab);
	}

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
