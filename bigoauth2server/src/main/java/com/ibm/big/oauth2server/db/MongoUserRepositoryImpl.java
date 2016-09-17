package com.ibm.big.oauth2server.db;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;

import com.ibm.big.oauth2server.beans.User;
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
	public long updatePassword(String userId, String newPwd)
	{
		logger.debug("updating password of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)),
				update("pwd", newPwd).set("updateddate", new Date()), User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long updatePhone(String userId, String phone)
	{
		logger.debug("updating password of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)),
				update("phone", phone).set("updateddate", new Date()), User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long addExpertise(String userId, String expertise)
	{
		logger.debug("updating password of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)),
				new Update().addToSet("aoe", expertise).set("updateddate", new Date()), User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public long addRole(String userId, String role)
	{
		logger.debug("updating password of user = " + userId);
		WriteResult wr = mongoTemplate.updateFirst(query(where("userId").is(userId)),
				new Update().addToSet("roles", role).set("updateddate", new Date()), User.class);
		logger.debug("update result = " + wr.getN());
		return wr.getN();
	}

	@Override
	public List<User> findUsersFTS(String any, int fetchLimit)
	{
		Query query = TextQuery.queryText(new TextCriteria().matching(any)).sortByScore().limit(fetchLimit);
		query.fields().exclude("pwd");
		return mongoTemplate.find(query, User.class);
	}

}