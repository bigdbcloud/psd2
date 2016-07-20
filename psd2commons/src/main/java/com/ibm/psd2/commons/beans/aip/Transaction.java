package com.ibm.psd2.commons.beans.aip;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_EMPTY)
public class Transaction implements Serializable
{
	private String id;
	private TransactionAccount this_account;
	private TransactionAccount other_account;
	private TransactionDetails details;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public TransactionAccount getThis_account()
	{
		return this_account;
	}
	public void setThis_account(TransactionAccount this_account)
	{
		this.this_account = this_account;
	}
	public TransactionAccount getOther_account()
	{
		return other_account;
	}
	public void setOther_account(TransactionAccount other_account)
	{
		this.other_account = other_account;
	}
	public TransactionDetails getDetails()
	{
		return details;
	}
	public void setDetails(TransactionDetails details)
	{
		this.details = details;
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
