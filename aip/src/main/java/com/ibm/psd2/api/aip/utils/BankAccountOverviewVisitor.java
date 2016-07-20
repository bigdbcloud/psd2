package com.ibm.psd2.api.aip.utils;

import com.ibm.psd2.commons.beans.aip.BankAccountDetailsBean;
import com.ibm.psd2.commons.beans.aip.BankAccountOverviewBean;
import com.ibm.psd2.commons.utils.Visitor;

public class BankAccountOverviewVisitor implements Visitor
{

	@Override
	public <T, U> T visit(U u)
	{
		BankAccountOverviewBean baob = new BankAccountOverviewBean();
		BankAccountDetailsBean bad = (BankAccountDetailsBean) u;
		
		baob.setBank_id(bad.getBank_id());
		baob.setId(bad.getId());
		baob.setLabel(bad.getLabel());
		
		return (T) baob;
	}

	
}
