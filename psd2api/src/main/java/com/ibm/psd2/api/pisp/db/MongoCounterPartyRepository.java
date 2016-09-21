package com.ibm.psd2.api.pisp.db;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.datamodel.pisp.CounterParty;

public interface MongoCounterPartyRepository extends MongoRepository<CounterParty, String>
{
	public List<CounterParty> findBySourceBankIdAndAccountId(String bankId, String accountId);
}
