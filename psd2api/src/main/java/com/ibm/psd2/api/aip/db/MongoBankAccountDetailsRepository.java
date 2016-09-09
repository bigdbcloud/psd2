package com.ibm.psd2.api.aip.db;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.beans.aip.BankAccountDetailsBean;

public interface MongoBankAccountDetailsRepository extends MongoRepository<BankAccountDetailsBean, Serializable>
{
	public BankAccountDetailsBean findByIdAndBank_id(String id, String bank_id);
	public List<BankAccountDetailsBean> findByUsernameAndBank_id(String username, String bank_id);
	public BankAccountDetailsBean findByIdAndBank_idAndUsername(String id, String bank_id, String username);
	
}
