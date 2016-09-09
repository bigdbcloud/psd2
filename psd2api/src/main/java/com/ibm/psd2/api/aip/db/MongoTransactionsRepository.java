package com.ibm.psd2.api.aip.db;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.beans.aip.TransactionBean;

public interface MongoTransactionsRepository extends MongoRepository<TransactionBean, Serializable>, MongoTransactionsRepositoryCustom
{
	public TransactionBean findByIdAndThis_accountIdAndThis_accountBankNational_identifier(String id, String accountId, String nationalIdentifier);
}
