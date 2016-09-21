package com.ibm.api.cashew.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.UserAccount;

public interface MongoUserAccountsRepository extends MongoRepository<UserAccount, String>
{
	public UserAccount findByAppUsernameAndAccountIdAndAccountBankIdAndAccountUsername(String username, String accountId, String bankId, String bankUsername);
	public UserAccount findByAccountIdAndAccountBankIdAndAccountUsername(String accountId, String bankId, String bankUsername);
	public UserAccount findByAccountIdAndAccountBankId(String accountId, String bankId);
	public UserAccount findByAppUsernameAndAccountIdAndAccountBankId(String appUser, String accountId, String bankId);
	public UserAccount findBySubscriptionRequestId(String subscriptionRequestId);
}
