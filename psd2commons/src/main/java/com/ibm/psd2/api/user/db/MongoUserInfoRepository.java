package com.ibm.psd2.api.user.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.datamodel.user.UserInfo;

public interface MongoUserInfoRepository extends MongoRepository<UserInfo, String>
{

}
