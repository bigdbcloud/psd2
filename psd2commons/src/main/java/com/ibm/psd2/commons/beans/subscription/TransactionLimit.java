package com.ibm.psd2.commons.beans.subscription;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.commons.beans.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionLimit implements Serializable
{

	private TransactionRequestType transaction_request_type;
	private Amount amount;
	
	public TransactionRequestType getTransaction_request_type()
	{
		return transaction_request_type;
	}
	public void setTransaction_request_type(TransactionRequestType transaction_request_type)
	{
		this.transaction_request_type = transaction_request_type;
	}
	public Amount getAmount()
	{
		return amount;
	}
	public void setAmount(Amount amount)
	{
		this.amount = amount;
	}
	
	
}
