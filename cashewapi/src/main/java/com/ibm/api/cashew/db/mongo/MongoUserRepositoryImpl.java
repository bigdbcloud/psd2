package com.ibm.api.cashew.db.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.Date;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;

import com.ibm.api.cashew.beans.Tag;
import com.ibm.api.cashew.beans.User;
import com.mongodb.WriteResult;

public class MongoUserRepositoryImpl implements MongoUserRepositoryCustom
{

	private static final Logger logger = LogManager.getLogger(MongoUserRepositoryImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public long updateUserLock(String userId, boolean locked)
	{
		logger.debug("updating lock status of user = " + userId + " to locked = " + locked);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)),
				update("locked", locked).set("updateddate", new Date()), User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long updatePhone(String userId, String phone)
	{
		logger.debug("updating phone of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)), update("mobileNumber", phone),
				User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long updateEmail(String userId, String email)
	{
		logger.debug("updating email of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)), update("email", email),
				User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long updateDOB(String userId, String dob)
	{
		logger.debug("updating email of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)), update("dateOfBirth", dob),
				User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long addTag(Set<Tag> tags, String userId)
	{

		Update up = new Update();
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)),
				up.pushAll("tags", tags.toArray()), User.class);

		return wr.hashCode();
	}

}
