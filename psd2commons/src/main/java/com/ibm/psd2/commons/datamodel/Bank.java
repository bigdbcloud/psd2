package com.ibm.psd2.commons.datamodel;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "Banks")
@JsonInclude(value = Include.NON_EMPTY)
public class Bank implements Serializable
{
	@Id
	String id;
	String short_name;
	String full_name;
	String logo;
	String website;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getShort_name()
	{
		return short_name;
	}
	public void setShort_name(String short_name)
	{
		this.short_name = short_name;
	}
	public String getFull_name()
	{
		return full_name;
	}
	public void setFull_name(String full_name)
	{
		this.full_name = full_name;
	}
	public String getLogo()
	{
		return logo;
	}
	public void setLogo(String logo)
	{
		this.logo = logo;
	}
	public String getWebsite()
	{
		return website;
	}
	public void setWebsite(String website)
	{
		this.website = website;
	}
	
	
	
}
