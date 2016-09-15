package com.ibm.api.cashew.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class APIRequest
{
	private User subject;

	public User getSubject()
	{
		return subject;
	}

	public void setSubject(User subject)
	{
		this.subject = subject;
	}

}
