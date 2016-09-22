package com.ibm.api.cashew.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.User;

public interface MongoUserRepository extends MongoRepository<User, String>, MongoUserRepositoryCustom
{
	public User findByEmail(String email);
	
}
