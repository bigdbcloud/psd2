package com.ibm.psd2.api.aip.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.aip.BankAccountDetailsBean;

public interface BankAccountDao
{
	public BankAccountDetailsBean getBankAccountDetails(String bankId, String accountId) throws Exception;
	public BankAccountDetailsBean getBankAccountDetails(String bankId, String accountId, String username) throws Exception;
	public void createBankAccountDetails(BankAccountDetailsBean b) throws Exception;
	public List<BankAccountDetailsBean> getBankAccounts(String username, String bank_id) throws Exception; 

}
