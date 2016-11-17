package com.ibm.psd2.api.integration.kafka;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoKafkaPropertiesRepository extends MongoRepository<KafkaProperties, String>
{
	public KafkaProperties findByUuid(String uuid);
}
