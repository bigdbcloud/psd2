package com.ibm.psd2.integration.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfig
{
	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception
	{
		MongoClientURI mcu = new MongoClientURI("mongodb://172.30.46.38:27017/psd2apirise");
		return new SimpleMongoDbFactory(mcu);
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception
	{
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		return mongoTemplate;
	}

}
