package com.ibm.psd2.api.integration;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoKafkaPropertiesRepository extends MongoRepository<KafkaProperties, String>
{
	public KafkaProperties findByUuid(String uuid);
}
