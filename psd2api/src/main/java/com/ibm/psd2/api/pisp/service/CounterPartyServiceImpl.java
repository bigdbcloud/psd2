package com.ibm.psd2.api.pisp.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.pisp.db.MongoCounterPartyRepository;
import com.ibm.psd2.datamodel.pisp.CounterParty;

@Service
public class CounterPartyServiceImpl implements CounterPartyService
{
	private final Logger logger = LogManager.getLogger(CounterPartyServiceImpl.class);
	
	@Autowired
	private MongoCounterPartyRepository mcpr;

	@Override
	public List<CounterParty> getCounterParties(String bankId, String accountId)
	{
		logger.debug("Parameters are: " + bankId + ", " + accountId);
		return mcpr.findBySourceBankIdAndSourceAccountId(bankId, accountId);
	}

	@Override
	public CounterParty createCounterParty(CounterParty cp)
	{
		logger.debug("Parameters are: " + cp);
		return mcpr.save(cp);
	}

}
