package com.ibm.big.oauth2server.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.big.oauth2server.beans.User;

public interface MongoUserRepository extends MongoRepository<User, String>, MongoUserRepositoryCustom
{
	public User findByEmail(String email);
}
