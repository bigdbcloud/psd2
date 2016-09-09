package com.ibm.psd2.commons.beans.aip;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Document(collection = "Transactions")
@JsonInclude(value = Include.NON_EMPTY)
public class TransactionBean implements Serializable
{
	@Id
	private String id;
	private TransactionAccountBean this_account;
	private TransactionAccountBean other_account;
	private TransactionDetailsBean details;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public TransactionAccountBean getThis_account()
	{
		return this_account;
	}
	public void setThis_account(TransactionAccountBean this_account)
	{
		this.this_account = this_account;
	}
	public TransactionAccountBean getOther_account()
	{
		return other_account;
	}
	public void setOther_account(TransactionAccountBean other_account)
	{
		this.other_account = other_account;
	}
	public TransactionDetailsBean getDetails()
	{
		return details;
	}
	public void setDetails(TransactionDetailsBean details)
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
