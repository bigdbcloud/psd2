package com.ibm.api.cashew.db.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.Tag;

public interface MongoTagRepository extends MongoRepository<Tag, String>
{

}
