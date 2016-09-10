package com.ibm.psd2.commons.datamodel.subscription;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.commons.datamodel.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionLimit implements Serializable
{

	private TransactionRequestType transactionRequestType;
	private Amount amount;
	
	public TransactionRequestType getTransactionRequestType()
	{
		return transactionRequestType;
	}
	public void setTransactionRequestType(TransactionRequestType transaction_request_type)
	{
		this.transactionRequestType = transaction_request_type;
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
