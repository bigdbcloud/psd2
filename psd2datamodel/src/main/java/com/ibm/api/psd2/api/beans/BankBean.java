package com.ibm.api.psd2.api.beans;

import java.io.Serializable;

public class BankBean implements Serializable, ResponseBean
{
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
