package com.ibm.psd2.api.aip.dao;

import java.util.List;

import com.ibm.psd2.commons.beans.BankBean;

public interface BankDao
{
	public BankBean getBankDetails(String bankId) throws Exception;
	public List<BankBean> getBanks() throws Exception;
	public BankBean createBank(BankBean b) throws Exception;
}
