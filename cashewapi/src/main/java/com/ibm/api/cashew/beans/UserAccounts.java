package com.ibm.api.cashew.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "UserAccounts")
@JsonInclude(value = Include.NON_EMPTY)
public class UserAccounts
{
	@Id
	private String userId;

	List<AccountSubscription> accountSubscriptions;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	public void addAccountSubscription(AccountSubscription accountSubscription)
	{
		if (accountSubscriptions == null)
		{
			accountSubscriptions = new ArrayList<>();
		}
		accountSubscriptions.add(accountSubscription);
	}

	public List<AccountSubscription> getBankAccounts()
	{
		return accountSubscriptions;
	}

	public void setBankAccounts(List<AccountSubscription> bankAccounts)
	{
		this.accountSubscriptions = bankAccounts;
	}
	
	
}
