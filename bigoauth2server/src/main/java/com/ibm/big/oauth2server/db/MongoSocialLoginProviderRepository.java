package com.ibm.big.oauth2server.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.big.oauth2server.beans.SocialLoginProvider;

public interface MongoSocialLoginProviderRepository extends MongoRepository<SocialLoginProvider, String>
{
}
