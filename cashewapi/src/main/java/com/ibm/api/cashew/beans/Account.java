package com.ibm.api.cashew.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@JsonInclude(value = Include.NON_EMPTY)
public class Account
{
	BankAccountDetailsView account;
	SubscriptionInfo subscription;
	
	SubscriptionRequest subscriptionRequest;

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
	
}
