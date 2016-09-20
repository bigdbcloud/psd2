package com.ibm.api.cashew.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class Address implements Serializable
{
	private String houseNumber;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String postcode;
	private String country;
	
	public String getHouseNumber()
	{
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber)
	{
		this.houseNumber = houseNumber;
	}
	public String getAddressLine1()
	{
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2()
	{
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getPostcode()
	{
		return postcode;
	}
	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}
	public String getCountry()
	{
		return country;
	}
	public void setCountry(String country)
	{
		this.country = country;
	}
}
