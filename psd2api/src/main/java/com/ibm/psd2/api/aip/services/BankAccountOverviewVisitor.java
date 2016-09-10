package com.ibm.psd2.api.aip.services;

import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.commons.datamodel.aip.BankAccountOverview;
import com.ibm.psd2.commons.utils.Visitor;

public class BankAccountOverviewVisitor implements Visitor
{

	@Override
	public <T, U> T visit(U u)
	{
		BankAccountOverview baob = new BankAccountOverview();
		BankAccountDetails bad = (BankAccountDetails) u;
		
		baob.setBankId(bad.getBankId());
		baob.setId(bad.getId());
		baob.setLabel(bad.getLabel());
		
		return (T) baob;
	}

	
}
