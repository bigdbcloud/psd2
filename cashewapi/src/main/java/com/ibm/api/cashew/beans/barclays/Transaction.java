package com.ibm.api.cashew.beans.barclays;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Transaction
{

	private Amount amount;

	private String id;

	private String customerId;

	private String created;

	private AccountBalanceAfterTransaction accountBalanceAfterTransaction;

	private String description;

	private String payee;

	private PaymentDescriptor paymentDescriptor;

	private String pingIt;

	private String notes;

	private String paymentMethod;

	private List<Metadata> metadata;

	public Amount getAmount()
	{
		return amount;
	}

	public void setAmount(Amount amount)
	{
		this.amount = amount;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	public String getCreated()
	{
		return created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	public AccountBalanceAfterTransaction getAccountBalanceAfterTransaction()
	{
		return accountBalanceAfterTransaction;
	}

	public void setAccountBalanceAfterTransaction(AccountBalanceAfterTransaction accountBalanceAfterTransaction)
	{
		this.accountBalanceAfterTransaction = accountBalanceAfterTransaction;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getPayee()
	{
		return payee;
	}

	public void setPayee(String payee)
	{
		this.payee = payee;
	}

	public PaymentDescriptor getPaymentDescriptor()
	{
		return paymentDescriptor;
	}

	public void setPaymentDescriptor(PaymentDescriptor paymentDescriptor)
	{
		this.paymentDescriptor = paymentDescriptor;
	}

	public String getPingIt()
	{
		return pingIt;
	}

	public void setPingIt(String pingIt)
	{
		this.pingIt = pingIt;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	public List<Metadata> getMetadata()
	{
		return metadata;
	}

	public void setMetadata(List<Metadata> metadata)
	{
		this.metadata = metadata;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}