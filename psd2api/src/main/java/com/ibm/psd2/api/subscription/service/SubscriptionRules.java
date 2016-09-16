package com.ibm.psd2.api.subscription.service;

import org.springframework.stereotype.Component;

import com.ibm.psd2.datamodel.ChallengeAnswer;

@Component
public class SubscriptionRules
{
	public boolean validateTxnChallengeAnswer(ChallengeAnswer t)
	{
		if (t.getAnswer() != null)
		{
			return true;
		}
		return false;
	}

}
