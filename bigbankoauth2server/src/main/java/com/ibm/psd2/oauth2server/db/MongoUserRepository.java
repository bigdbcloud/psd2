package com.ibm.psd2.oauth2server.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.oauth2server.beans.UserInfo;

public interface MongoUserRepository extends MongoRepository<UserInfo, String>
{

}
