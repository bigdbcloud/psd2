package com.ibm.psd2.api.aip.db;

public interface MongoBankAccountDetailsRepositoryCustom
{
	public void updateBalance(String bankId, String accountId, double balance);
}
