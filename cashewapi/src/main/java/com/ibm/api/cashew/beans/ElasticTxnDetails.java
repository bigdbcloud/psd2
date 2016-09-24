package com.ibm.api.cashew.beans;

import java.util.Date;

import com.ibm.psd2.datamodel.Amount;

public class ElasticTxnDetails
{


	private String type;
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getPosted()
	{
		return posted;
	}
	public void setPosted(String posted)
	{
		this.posted = posted;
	}
	public String getCompleted()
	{
		return completed;
	}
	public void setCompleted(String completed)
	{
		this.completed = completed;
	}
	public Amount getNewBalance()
	{
		return newBalance;
	}
	public void setNewBalance(Amount newBalance)
	{
		this.newBalance = newBalance;
	}
	public Amount getValue()
	{
		return value;
	}
	public void setValue(Amount value)
	{
		this.value = value;
	}
	public String getTag()
	{
		return tag;
	}
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	private String description;
	private String posted;
	private String completed;
	private Amount newBalance;
	private Amount value;
	private String tag;
}
