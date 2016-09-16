package com.ibm.api.cashew.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.UserAccounts;

public interface MongoUserAccountsRepository extends MongoRepository<UserAccounts, String>
{
	
}
