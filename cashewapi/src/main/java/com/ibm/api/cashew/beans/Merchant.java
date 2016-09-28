package com.ibm.api.cashew.beans;

public class Merchant
{
	String id;
	String name;
	String category;
	String merchantProviderBankId;
	String accountId;
	String bankId;
	Address address;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getMerchantProviderBankId()
	{
		return merchantProviderBankId;
	}

	public void setMerchantProviderBankId(String merchantProviderBankId)
	{
		this.merchantProviderBankId = merchantProviderBankId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getBankId()
	{
		return bankId;
	}

	public void setBankId(String bankId)
	{
		this.bankId = bankId;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

}
