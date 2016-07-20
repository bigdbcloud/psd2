package com.ibm.psd2.commons.beans.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnParty implements Serializable
{
	private String bank_id;
	private String account_id;
	
	public TxnParty()
	{
		
	}
	
	public TxnParty(String bank_id, String account_id)
	{
		this.bank_id = bank_id;
		this.account_id = account_id;
	}
	
	public String getBank_id()
	{
		return bank_id;
	}
	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}
	public String getAccount_id()
	{
		return account_id;
	}
	public void setAccount_id(String account_id)
	{
		this.account_id = account_id;
	}
	
	
}
