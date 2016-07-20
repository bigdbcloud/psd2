package com.ibm.psd2.api.aip.utils;

import com.ibm.psd2.commons.beans.aip.*;
import com.ibm.psd2.commons.utils.Visitor;

public class BankAccountOwnerViewVisitor implements Visitor
{
	@Override
	public <T, U> T visit(U u)
	{
		
		BankAccountDetailsViewBean badv = new BankAccountDetailsViewBean();
		BankAccountDetailsBean bad = (BankAccountDetailsBean) u;
		
		badv.setId(bad.getId());
		badv.setBank_id(bad.getBank_id());
		badv.setBalance(bad.getBalance());
		badv.setIban(bad.getIban());
		badv.setLabel(bad.getLabel());
		badv.setNumber(bad.getNumber());
		badv.setOwners(bad.getOwners());
		badv.setSwift_bic(bad.getSwift_bic());
		badv.setType(bad.getType());
		return (T) badv;
	}
	
}
