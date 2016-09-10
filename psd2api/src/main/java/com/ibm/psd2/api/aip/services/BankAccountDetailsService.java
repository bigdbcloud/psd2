package com.ibm.psd2.api.aip.services;

import java.util.List;

import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;

public interface BankAccountDetailsService
{
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId) throws Exception;
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId, String username) throws Exception;
	public void createBankAccountDetails(BankAccountDetails b) throws Exception;
	public List<BankAccountDetails> getBankAccounts(String username, String bank_id) throws Exception; 

}
