package com.ibm.psd2.datamodel.aip;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_EMPTY)
public class TransactionAccount implements Serializable
{
	String id;
	private ArrayList<BankAccountOwner> holders;
	String number;
	String kind;
	String iban;
	String swiftBic;
	TransactionBank bank;

	public void addHolders(BankAccountOwner b)
	{
		if (holders == null)
		{
			holders = new ArrayList<>();
		}
		holders.add(b);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public ArrayList<BankAccountOwner> getHolders()
	{
		return holders;
	}

	public void setHolders(ArrayList<BankAccountOwner> holders)
	{
		this.holders = holders;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getKind()
	{
		return kind;
	}

	public void setKind(String kind)
	{
		this.kind = kind;
	}

	public String getIban()
	{
		return iban;
	}

	public void setIban(String iban)
	{
		this.iban = iban;
	}

	public String getSwiftBic()
	{
		return swiftBic;
	}

	public void setSwiftBic(String swift_bic)
	{
		this.swiftBic = swift_bic;
	}

	public TransactionBank getBank()
	{
		return bank;
	}

	public void setBank(TransactionBank bank)
	{
		this.bank = bank;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

}
