package com.ibm.psd2.datamodel.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.Amount;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnCharge extends AbstractPISPEntity implements Serializable
{
	private String summary;
	private Amount value;

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public Amount getValue()
	{
		return value;
	}

	public void setValue(Amount value)
	{
		this.value = value;
	}

}
