package com.ibm.psd2.api.aip.services;

import com.ibm.psd2.datamodel.aip.*;
import com.ibm.psd2.utils.Visitor;

public class BankAccountOwnerViewVisitor implements Visitor
{
	@Override
	public <T, U> T visit(U u)
	{

		BankAccountDetailsView badv = new BankAccountDetailsView();
		BankAccountDetails bad = (BankAccountDetails) u;

		badv.setId(bad.getId());
		badv.setBankId(bad.getBankId());
		badv.setBalance(bad.getBalance());
		badv.setIban(bad.getIban());
		badv.setLabel(bad.getLabel());
		badv.setNumber(bad.getNumber());
		badv.setOwners(bad.getOwners());
		badv.setSwiftBic(bad.getSwiftBic());
		badv.setType(bad.getType());
		return (T) badv;
	}

}
