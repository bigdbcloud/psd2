package com.ibm.psd2.datamodel;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class Challenge implements Serializable
{
	public static final int CHALLENGE_MAX_ATTEMPTS = 3;
	public static final String ACCOUNT_SUBSCRIPTION = "ACCOUNT_SUBSCRIPTION";
	public static final String TXN_CHALLENGE = "TXN_CHALLENGE";
	public static final String CHALLENGE_SUBSCRIPTION = "Secret answer for subscription request initiated by you is {0}.";
	public static final String CHALLENGE_TXN = "Secret answer for transaction request initiated by you is {0}.";

	private String id;
	private int allowedAttempts;
	private String challengeType;
	private String answer;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public int getAllowedAttempts()
	{
		return allowedAttempts;
	}

	public void setAllowedAttempts(int allowed_attempts)
	{
		this.allowedAttempts = allowed_attempts;
	}

	public String getChallengeType()
	{
		return challengeType;
	}

	public void setChallengeType(String challenge_type)
	{
		this.challengeType = challenge_type;
	}

	public String getAnswer()
	{
		return answer;
	}

	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
}
