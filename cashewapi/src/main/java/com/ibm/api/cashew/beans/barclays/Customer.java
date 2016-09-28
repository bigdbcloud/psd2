package com.ibm.api.cashew.beans.barclays;

import java.util.List;

public class Customer
{

	private String dateOfBirth;

	private String id;

	private String lastName;

	private String title;

	private List<Account> accountList;

	private String isNewToBank;

	private String nationalityCode;

	private Address address;

	private String firstName;

	private String mobileNo;

	private String middleNames;

	public String getDateOfBirth()
	{
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getIsNewToBank()
	{
		return isNewToBank;
	}

	public void setIsNewToBank(String isNewToBank)
	{
		this.isNewToBank = isNewToBank;
	}

	public String getNationalityCode()
	{
		return nationalityCode;
	}

	public void setNationalityCode(String nationalityCode)
	{
		this.nationalityCode = nationalityCode;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getMobileNo()
	{
		return mobileNo;
	}

	public void setMobileNo(String mobileNo)
	{
		this.mobileNo = mobileNo;
	}

	public String getMiddleNames()
	{
		return middleNames;
	}

	public void setMiddleNames(String middleNames)
	{
		this.middleNames = middleNames;
	}

	public List<Account> getAccountList()
	{
		return accountList;
	}

	public void setAccountList(List<Account> accountList)
	{
		this.accountList = accountList;
	}

	@Override
	public String toString()
	{
		return "Customer [dateOfBirth=" + dateOfBirth + ", id=" + id + ", lastName=" + lastName + ", title=" + title
				+ ", accountList=" + accountList + ", isNewToBank=" + isNewToBank + ", nationalityCode="
				+ nationalityCode + ", address=" + address + ", firstName=" + firstName + ", mobileNo=" + mobileNo
				+ ", middleNames=" + middleNames + "]";
	}
}
