package com.ibm.api.cashew.bank.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.datamodel.aip.Transaction;

public interface MongoTransactionRepository extends MongoRepository<Transaction,String> {

}
