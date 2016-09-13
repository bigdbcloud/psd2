package com.ibm.psd2.api.aip.services;

import java.util.List;

import com.ibm.psd2.commons.datamodel.Bank;

public interface BankService
{
	public Bank getBankDetails(String bankId) throws Exception;

	public List<Bank> getBanks() throws Exception;

	public Bank createBank(Bank b) throws Exception;
}
