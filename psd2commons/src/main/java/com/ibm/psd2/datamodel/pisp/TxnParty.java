package com.ibm.psd2.datamodel.pisp;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class TxnParty implements Serializable
{
	/*
		"to":{
				"bankId":"psd201-bank-x--uk",
	    		"accountId":"007007007007007007007"
			},  
	 */
	
	private String bankId;
	private String accountId;
	
	public TxnParty()
	{
		
	}
	
	public TxnParty(String bank_id, String account_id)
	{
		this.bankId = bank_id;
		this.accountId = account_id;
	}
	
	public String getBankId()
	{
		return bankId;
	}
	public void setBankId(String bank_id)
	{
		this.bankId = bank_id;
	}
	public String getAccountId()
	{
		return accountId;
	}
	public void setAccountId(String account_id)
	{
		this.accountId = account_id;
	}
	
	
}
