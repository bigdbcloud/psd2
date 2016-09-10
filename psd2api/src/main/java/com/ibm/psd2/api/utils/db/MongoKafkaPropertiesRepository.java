package com.ibm.psd2.api.utils.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.psd2.api.utils.KafkaProperties;

public interface MongoKafkaPropertiesRepository extends MongoRepository<KafkaProperties, String>
{
	public KafkaProperties findByUuid(String uuid);
}
