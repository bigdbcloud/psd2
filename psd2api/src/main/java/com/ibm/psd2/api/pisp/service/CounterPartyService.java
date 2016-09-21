package com.ibm.psd2.api.pisp.service;

import java.util.List;

import com.ibm.psd2.datamodel.pisp.CounterParty;

public interface CounterPartyService
{
	public List<CounterParty> getCounterParties(String bankId, String accountId);
}
