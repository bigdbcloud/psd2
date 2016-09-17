package com.ibm.api.cashew.beans;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@Document(collection = "UserAccounts")
@JsonInclude(value = Include.NON_EMPTY)
public class UserAccount implements Serializable
{
	private String appUsername;

	BankAccountDetailsView account;
	SubscriptionInfo subscription;
	SubscriptionRequest subscriptionRequest;

	public String getAppUsername()
	{
		return appUsername;
	}

	public void setAppUsername(String userId)
	{
		this.appUsername = userId;
	}

	public BankAccountDetailsView getAccount()
	{
		return account;
	}

	public void setAccount(BankAccountDetailsView account)
	{
		this.account = account;
	}

	public SubscriptionInfo getSubscription()
	{
		return subscription;
	}

	public void setSubscription(SubscriptionInfo subscription)
	{
		this.subscription = subscription;
	}

	public SubscriptionRequest getSubscriptionRequest()
	{
		return subscriptionRequest;
	}

	public void setSubscriptionRequest(SubscriptionRequest subscriptionRequest)
	{
		this.subscriptionRequest = subscriptionRequest;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
