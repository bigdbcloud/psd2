package com.ibm.api.cashew.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class APIResponse<T>
{
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_ERROR = "ERROR";

	private String status;
	private String errMsg;
	private String errDetails;
	private T response;
	private String version;

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getErrMsg()
	{
		return errMsg;
	}

	public void setErrMsg(String errMsg)
	{
		this.errMsg = errMsg;
	}

	public String getErrDetails()
	{
		return errDetails;
	}

	public void setErrDetails(String errDetails)
	{
		this.errDetails = errDetails;
	}

	public T getResponse()
	{
		return response;
	}

	public void setResponse(T response)
	{
		this.response = response;
	}
}
