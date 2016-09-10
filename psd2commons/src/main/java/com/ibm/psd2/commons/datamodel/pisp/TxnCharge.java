package com.ibm.psd2.commons.datamodel.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.commons.datamodel.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnCharge implements Serializable
{

	/*
		  "charge":{
		    "summary":"Total charges for completed transaction",
		    "value":{
		      "currency":"EUR",
		      "amount":"0.010053"
		    }
		  }
	*/
	
	private String summary;
	private Amount value;
	
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public Amount getValue()
	{
		return value;
	}
	public void setValue(Amount value)
	{
		this.value = value;
	}
	
	
}
