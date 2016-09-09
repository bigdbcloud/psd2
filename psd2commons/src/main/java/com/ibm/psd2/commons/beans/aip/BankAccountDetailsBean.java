package com.ibm.psd2.commons.beans.aip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.commons.beans.AmountBean;
import com.ibm.psd2.commons.utils.Visitor;

@Document(collection = "BankAccountDetails")
@JsonInclude(value = Include.NON_EMPTY)
public class BankAccountDetailsBean implements Serializable
{
	private String id;

	private String label;
	private String number;
	private ArrayList<BankAccountOwnerBean> owners;
	private String type;
	private AmountBean balance;
	private String iban;
	private String swift_bic;
	private String bank_id;
	private String username;

	@JsonIgnore
	private Map<String, Visitor> visitors;

	public void addOwners(BankAccountOwnerBean b)
	{
		if (owners == null)
		{
			owners = new ArrayList<>();
		}
		owners.add(b);
	}

	public void registerVisitor(String s, Visitor v)
	{
		if (visitors == null)
		{
			visitors = new HashMap<>();
		}
		visitors.put(s, v);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public ArrayList<BankAccountOwnerBean> getOwners()
	{
		return owners;
	}

	public void setOwners(ArrayList<BankAccountOwnerBean> owners)
	{
		this.owners = owners;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public AmountBean getBalance()
	{
		return balance;
	}

	public void setBalance(AmountBean balance)
	{
		this.balance = balance;
	}

	public String getIban()
	{
		return iban;
	}

	public void setIban(String iban)
	{
		this.iban = iban;
	}

	public String getSwift_bic()
	{
		return swift_bic;
	}

	public void setSwift_bic(String swift_bic)
	{
		this.swift_bic = swift_bic;
	}

	public String getBank_id()
	{
		return bank_id;
	}

	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public BankAccountOverviewBean getBankAccountOverview(String viewId)
	{
		Visitor v = visitors.get(BankAccountOverviewBean.class.getName() + ":" + viewId);

		if (v == null)
		{
			throw new IllegalStateException("No visitor set for BankAccountOverview Bean");
		}
		return (BankAccountOverviewBean) v.visit(this);
	}

	public BankAccountDetailsViewBean getBankAccountDetails(String viewId)
	{
		Visitor v = visitors.get(BankAccountDetailsViewBean.class.getName() + ":" + viewId);

		if (v == null)
		{
			throw new IllegalStateException("No visitor set for BankAccountDetailsBean");
		}

		return (BankAccountDetailsViewBean) v.visit(this);
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
