package com.ibm.psd2.api.subscriptionmgmt;

import org.springframework.stereotype.Component;

import com.ibm.psd2.commons.datamodel.ChallengeAnswer;

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
