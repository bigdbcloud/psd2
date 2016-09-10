package com.ibm.psd2.api.subscription.db;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.ibm.psd2.commons.datamodel.subscription.SubscriptionRequest;
import com.mongodb.WriteResult;

public class MongoSubscriptionRequestRepositoryImpl implements MongoSubscriptionRequestRepositoryCustom
{
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public int updateStatus(String id, String status)
	{
		WriteResult wr = mongoTemplate.updateFirst(query(where("id").is(id)), update("status", status).currentDate("updatedDate"), SubscriptionRequest.class);
		return wr.getN();
	}

	
}
