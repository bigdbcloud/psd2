package com.ibm.psd2.api.aip.db;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.aip.Transaction;

public interface MongoTransactionsRepository extends MongoRepository<Transaction, Serializable>, MongoTransactionsRepositoryCustom
{
	public Transaction findByIdAndThisAccountIdAndThisAccountBankNationalIdentifier(String id, String accountId, String nationalIdentifier);
}
