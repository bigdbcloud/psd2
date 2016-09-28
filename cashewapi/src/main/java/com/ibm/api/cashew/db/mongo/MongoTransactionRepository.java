package com.ibm.api.cashew.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.datamodel.aip.Transaction;

public interface MongoTransactionRepository extends MongoRepository<Transaction, String>
{

}
