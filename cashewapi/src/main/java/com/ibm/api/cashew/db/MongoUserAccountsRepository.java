package com.ibm.api.cashew.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.UserAccount;

public interface MongoUserAccountsRepository extends MongoRepository<UserAccount, String>
{
	public UserAccount findByAppUsernameAndAccountIdAndAccountBankIdAndAccountUsername(String username, String accountId, String bankId, String bankUsername);
	public UserAccount findByAppUsernameAndAccountIdAndAccountBankId(String appUser, String accountId, String bankId);
}
