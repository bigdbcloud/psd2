package com.ibm.psd2.api.aip.db;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;

public interface MongoBankAccountDetailsRepository extends MongoRepository<BankAccountDetails, Serializable>, MongoBankAccountDetailsRepositoryCustom
{
	public BankAccountDetails findByIdAndBankId(String id, String bank_id);

	public List<BankAccountDetails> findByUsernameAndBankId(String username, String bank_id);

	public BankAccountDetails findByIdAndBankIdAndUsername(String id, String bank_id, String username);

}
