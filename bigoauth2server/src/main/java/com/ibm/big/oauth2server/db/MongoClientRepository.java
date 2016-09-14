package com.ibm.big.oauth2server.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.big.oauth2server.beans.Client;

public interface MongoClientRepository extends MongoRepository<Client, String>
{
}
