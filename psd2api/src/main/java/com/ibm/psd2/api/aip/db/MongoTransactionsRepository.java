package com.ibm.psd2.api.aip.db;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.aip.TransactionBean;

public interface MongoTransactionsRepository extends MongoRepository<TransactionBean, Serializable>, MongoTransactionsRepositoryCustom
{
	public TransactionBean findByIdAndThisAccountIdAndThisAccountBankNationalIdentifier(String id, String accountId, String nationalIdentifier);
}
