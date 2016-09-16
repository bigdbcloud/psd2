package com.ibm.api.cashew.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;

@JsonInclude(value = Include.NON_EMPTY)
public class AccountSubscription
{
	boolean isSubscribed;
	
	BankAccountDetailsView account;

	public boolean isSubscribed()
	{
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed)
	{
		this.isSubscribed = isSubscribed;
	}

	public BankAccountDetailsView getAccount()
	{
		return account;
	}

	public void setAccount(BankAccountDetailsView account)
	{
		this.account = account;
	}
	
	

}
