package com.ibm.psd2.api.aip.db;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.datamodel.Bank;

public interface MongoBankRepository extends MongoRepository<Bank, Serializable>
{
}
