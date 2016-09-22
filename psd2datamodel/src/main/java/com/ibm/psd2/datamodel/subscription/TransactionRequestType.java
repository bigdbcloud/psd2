package com.ibm.psd2.datamodel.subscription;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionRequestType implements Serializable
{
	public static enum TYPES
	{
		SELF("SELF"), MERCHANT("MERCHANT"), PAYEE("PAYEE");

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

	public static boolean isValid(String val)
	{
		if (val != null && !val.isEmpty())
		{
			TYPES[] types = TYPES.values();
			for (int i = 0; i < types.length; i++)
			{
				if (types[i].type().equals(val))
				{
					return true;
				}
			}
		}
		return false;
	}

}
