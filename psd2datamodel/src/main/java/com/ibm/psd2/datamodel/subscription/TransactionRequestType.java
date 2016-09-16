package com.ibm.psd2.datamodel.subscription;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionRequestType implements Serializable
{
	public static enum TYPES 
	{
		WITHIN_BANK("WITHIN_BANK"),
		INTER_BANK("INTER_BANK"),
		INTERNATIONAL("INTERNATIONAL");
		
		private String type;
		
		public String type()
		{
			return type;
		}
		
		TYPES(String type)
		{
			this.type = type;
		};
	}

	private String value;

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	
}
