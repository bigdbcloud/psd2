package com.ibm.api.cashew.vocher.db;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ibm.api.cashew.beans.TxnDetails;
import com.ibm.api.cashew.beans.Vocher;
import com.mongodb.WriteResult;

public class MongoVocherRepositoryImpl implements MongoVocherCustomRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public int updateVocher(Vocher vocher, TxnDetails redeemTo) {
		
		WriteResult wr = mongoTemplate.updateFirst(query(where("code").is(vocher.getCode())),
				update("isRedeemed",vocher.isRedeemed()).push("redeemedTo", redeemTo), Vocher.class);
		return wr.hashCode();
	}

}
