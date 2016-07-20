package com.ibm.psd2.api.subscriptionmgmt;

import org.springframework.stereotype.Component;

import com.ibm.psd2.commons.beans.ChallengeAnswerBean;

@Component
public class SubscriptionRules
{
	public boolean validateTxnChallengeAnswer(ChallengeAnswerBean t)
	{
		if (t.getAnswer() != null)
		{
			return true;
		}
		return false;
	}

}
