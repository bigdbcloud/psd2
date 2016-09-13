package com.ibm.psd2.api.security;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoAPIClientRepository extends MongoRepository<APIClient, String>
{

}
