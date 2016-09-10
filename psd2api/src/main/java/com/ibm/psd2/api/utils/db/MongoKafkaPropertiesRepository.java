package com.ibm.psd2.api.utils.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.commons.datamodel.KafkaProperties;

public interface MongoKafkaPropertiesRepository extends MongoRepository<KafkaProperties, String>
{
}
