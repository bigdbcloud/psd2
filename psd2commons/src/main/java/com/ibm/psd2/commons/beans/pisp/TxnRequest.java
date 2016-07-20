package com.ibm.psd2.commons.beans.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.commons.beans.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnRequest implements Serializable
{
	private TxnParty to;
	private Amount value;
	private String description;
	
	private String transaction_request_type;
	
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

	public String getTransaction_request_type()
	{
		return transaction_request_type;
	}
	
	public void setTransaction_request_type(String transaction_request_type)
	{
		this.transaction_request_type = transaction_request_type;
	}

}
