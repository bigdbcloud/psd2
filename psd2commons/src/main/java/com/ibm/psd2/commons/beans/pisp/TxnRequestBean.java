package com.ibm.psd2.commons.beans.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.commons.beans.AmountBean;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnRequestBean implements Serializable
{
	/*
		"to":{
				"bank_id":"psd201-bank-x--uk",
    			"account_id":"007007007007007007007"
			},  
		"value":{
    		"currency":"EUR",
    		"amount":"100.53"
			},
		"description":"A description for the transaction to be created"
	*/
	
	private TxnPartyBean to;
	private AmountBean value;
	private String description;
	
	private String transaction_request_type;
	
	public TxnPartyBean getTo()
	{
		return to;
	}
	
	public void setTo(TxnPartyBean to)
	{
		this.to = to;
	}
	
	public AmountBean getValue()
	{
		return value;
	}
	
	public void setValue(AmountBean value)
	{
		this.value = value;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTransaction_request_type()
	{
		return transaction_request_type;
	}
	
	public void setTransaction_request_type(String transaction_request_type)
	{
		this.transaction_request_type = transaction_request_type;
	}

}
