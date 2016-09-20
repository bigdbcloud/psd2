package com.ibm.api.cashew.beans;

import com.ibm.psd2.datamodel.Amount;

public class TxnDetails {

	private String bankId;
	private String accountId;
	private Amount value;

	public Amount getValue() {
		return value;
	}

	public void setValue(Amount value) {
		this.value = value;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	
}
