package com.ibm.api.cashew.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.subscription.TransactionLimit;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;
import com.ibm.psd2.datamodel.subscription.ViewId;

@Document(collection = "UserAccounts")
@JsonInclude(value = Include.NON_EMPTY)
public class UserAccount implements Serializable
{
	@Id
	String id;
	private String appUsername;

	BankAccountDetailsView account;

	String subscriptionRequestId;
	String subscriptionRequestStatus;
	String subscriptionRequestChallengeId;

	String subscriptionInfoStatus;

	private List<TransactionRequestType> transactionRequestTypes;
	private List<TransactionLimit> limits;
	private ArrayList<ViewId> viewIds;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

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

	public String getSubscriptionRequestId()
	{
		return subscriptionRequestId;
	}

	public void setSubscriptionRequestId(String subscriptionRequestId)
	{
		this.subscriptionRequestId = subscriptionRequestId;
	}

	public String getSubscriptionRequestStatus()
	{
		return subscriptionRequestStatus;
	}

	public void setSubscriptionRequestStatus(String subscriptionRequestStatus)
	{
		this.subscriptionRequestStatus = subscriptionRequestStatus;
	}

	public String getSubscriptionInfoStatus()
	{
		return subscriptionInfoStatus;
	}

	public void setSubscriptionInfoStatus(String subscriptionInfoStatus)
	{
		this.subscriptionInfoStatus = subscriptionInfoStatus;
	}

	public List<TransactionRequestType> getTransactionRequestTypes()
	{
		return transactionRequestTypes;
	}

	public void setTransactionRequestTypes(List<TransactionRequestType> transactionRequestTypes)
	{
		this.transactionRequestTypes = transactionRequestTypes;
	}

	public List<TransactionLimit> getLimits()
	{
		return limits;
	}

	public void setLimits(List<TransactionLimit> limits)
	{
		this.limits = limits;
	}

	public ArrayList<ViewId> getViewIds()
	{
		return viewIds;
	}

	public void setViewIds(ArrayList<ViewId> viewIds)
	{
		this.viewIds = viewIds;
	}

	public String getSubscriptionRequestChallengeId()
	{
		return subscriptionRequestChallengeId;
	}

	public void setSubscriptionRequestChallengeId(String subscriptionRequestChallengeId)
	{
		this.subscriptionRequestChallengeId = subscriptionRequestChallengeId;
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
