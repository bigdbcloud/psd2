package com.ibm.psd2.api.pisp.db;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.pisp.TxnRequestDetails;

public interface MongoTxnRequestDetailsRepository
		extends MongoRepository<TxnRequestDetails, String>, MongoTxnRequestDetailsRepositoryCustom
{

	public TxnRequestDetails findByFromAccountIdAndFromBankIdAndTypeAndIdAndChallengeId(String accountId, String bankId,
			String type, String id, String challengeId);

	public List<TxnRequestDetails> findByFromAccountIdAndFromBankId(String accountId, String bankId);

}
