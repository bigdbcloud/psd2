package com.ibm.psd2.datamodel.aip;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.datamodel.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionDetails implements Serializable
{
	private String type;
	private String description;
	private String posted;
	private String completed;
	private Amount newBalance;
	private Amount value;
	private String tag;
	
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
	
	public Amount getNewBalance()
	{
		return newBalance;
	}
	public void setNewBalance(Amount new_balance)
	{
		this.newBalance = new_balance;
	}
	public Amount getValue()
	{
		return value;
	}
	public void setValue(Amount value)
	{
		this.value = value;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
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
	public String getPosted() {
		return posted;
	}
	public void setPosted(String posted) {
		this.posted = posted;
	}
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}
	
	
}
