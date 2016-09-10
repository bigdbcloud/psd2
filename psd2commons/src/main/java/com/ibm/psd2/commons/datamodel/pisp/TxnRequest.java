package com.ibm.psd2.commons.datamodel.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.commons.datamodel.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnRequest implements Serializable
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
	
	private TxnParty to;
	private Amount value;
	private String description;
	
	private String transactionRequestType;
	
	public TxnParty getTo()
	{
		return to;
	}
	
	public void setTo(TxnParty to)
	{
		this.to = to;
	}
	
	public Amount getValue()
	{
		return value;
	}
	
	public void setValue(Amount value)
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

	public String getTransactionRequestType()
	{
		return transactionRequestType;
	}
	
	public void setTransactionRequestType(String transaction_request_type)
	{
		this.transactionRequestType = transaction_request_type;
	}

}
