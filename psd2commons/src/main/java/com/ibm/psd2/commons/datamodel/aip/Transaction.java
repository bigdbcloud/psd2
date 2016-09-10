package com.ibm.psd2.commons.datamodel.aip;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document(collection = "Transactions")
@JsonInclude(value = Include.NON_EMPTY)
public class Transaction implements Serializable
{
	@Id
	private String id;
	private TransactionAccount thisAccount;
	private TransactionAccount otherAccount;
	private TransactionDetails details;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public TransactionAccount getThisAccount()
	{
		return thisAccount;
	}
	public void setThisAccount(TransactionAccount this_account)
	{
		this.thisAccount = this_account;
	}
	public TransactionAccount getOtherAccount()
	{
		return otherAccount;
	}
	public void setOtherAccount(TransactionAccount other_account)
	{
		this.otherAccount = other_account;
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
