package com.ibm.psd2.commons.datamodel.aip;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionBank implements Serializable
{
	String nationalIdentifier;
	String name;
	public String getNationalIdentifier()
	{
		return nationalIdentifier;
	}
	public void setNationalIdentifier(String national_identifier)
	{
		this.nationalIdentifier = national_identifier;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

}
