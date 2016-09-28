package com.ibm.psd2.datamodel;

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
	String shortName;
	String fullName;
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

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String short_name)
	{
		this.shortName = short_name;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String full_name)
	{
		this.fullName = full_name;
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
