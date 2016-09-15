package com.ibm.psd2.api.aip.services;

import java.util.List;

import com.ibm.psd2.datamodel.aip.BankAccountDetails;

public interface BankAccountDetailsService
{
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId);

	public BankAccountDetails getBankAccountDetails(String bankId, String accountId, String username);

	public void createBankAccountDetails(BankAccountDetails b);

	public List<BankAccountDetails> getBankAccounts(String username, String bank_id);
	
	public void updateBalance(String bankId, String accountId, double balance);

}
