package com.ibm.psd2.api.aip.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.Bank;

public interface BankDao
{
	public Bank getBankDetails(String bankId) throws Exception;
	public List<Bank> getBanks() throws Exception;
	public Bank createBank(Bank b) throws Exception;
}
