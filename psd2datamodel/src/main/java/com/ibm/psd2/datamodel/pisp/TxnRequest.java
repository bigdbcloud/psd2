package com.ibm.psd2.datamodel.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnRequest extends AbstractPISPEntity implements Serializable
{

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
