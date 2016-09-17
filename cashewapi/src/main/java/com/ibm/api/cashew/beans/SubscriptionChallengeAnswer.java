package com.ibm.api.cashew.beans;

import com.ibm.psd2.datamodel.ChallengeAnswer;

public class SubscriptionChallengeAnswer
{
	String appUsername;
	String subscriptionRequestId;
	ChallengeAnswer challengeAnswer;

	public String getAppUsername()
	{
		return appUsername;
	}

	public void setAppUsername(String appUsername)
	{
		this.appUsername = appUsername;
	}

	public String getSubscriptionRequestId()
	{
		return subscriptionRequestId;
	}

	public void setSubscriptionRequestId(String subscriptionRequestId)
	{
		this.subscriptionRequestId = subscriptionRequestId;
	}

	public ChallengeAnswer getChallengeAnswer()
	{
		return challengeAnswer;
	}

	public void setChallengeAnswer(ChallengeAnswer challengeAnswer)
	{
		this.challengeAnswer = challengeAnswer;
	}
}
