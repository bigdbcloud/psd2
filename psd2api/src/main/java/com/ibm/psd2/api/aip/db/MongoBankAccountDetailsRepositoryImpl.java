package com.ibm.psd2.api.aip.db;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ibm.psd2.datamodel.aip.BankAccountDetails;

public class MongoBankAccountDetailsRepositoryImpl implements MongoBankAccountDetailsRepositoryCustom
{
	@Autowired
	private MongoTemplate mongoTemplate;


	@Override
	public void updateBalance(String bankId, String accountId, double balance)
	{
		mongoTemplate.updateFirst(query(where("id").is(accountId).and("bankId").is(bankId)), update("balance.amount", balance), BankAccountDetails.class);
	}

	
}
