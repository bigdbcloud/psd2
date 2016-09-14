package com.ibm.api.oauth2server.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.oauth2server.beans.ClientInfo;

public interface MongoClientRepository extends MongoRepository<ClientInfo, String>
{

}
