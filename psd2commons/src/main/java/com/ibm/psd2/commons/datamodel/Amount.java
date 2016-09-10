package com.ibm.psd2.commons.datamodel;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class Amount implements Serializable
{
	/*
		"value":{
			"currency":"EUR",
			"amount":"100.53"
			},
	*/

	private String currency;
	private double amount;
	
	public String getCurrency()
	{
		return currency;
	}
	
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	
	public double getAmount()
	{
		return amount;
	}
	
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
}
