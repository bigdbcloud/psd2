package com.ibm.psd2.api.aip.db;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.beans.BankBean;

public interface MongoBankRepository extends MongoRepository<BankBean, Serializable>
{
}
