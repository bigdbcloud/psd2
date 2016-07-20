package com.ibm.psd2.api.commons.integration;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.commons.db.MongoConnection;
import com.ibm.psd2.api.commons.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.KafkaProperties;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Component
public class PropertiesDaoImpl implements PropertiesDao
{
	private static final Logger logger = LogManager.getLogger(PropertiesDaoImpl.class);

	@Autowired
	private MongoConnection conn;

	@Value("${mongodb.collection.kafkaproperties}")
	private String collection;
	
	@Autowired
	private MongoDocumentParser mdp;

	@Override
	public Properties getKafkaProperties() throws Exception
	{
		MongoCollection<Document> coll = conn.getDB().getCollection(collection);
		logger.info("Fetching Kafka Properties");
		FindIterable<Document> iterable = coll.find(eq("uuid", "kafka-properties")).projection(excludeId());
		Document doc = iterable.first();
		KafkaProperties kp = mdp.parse(doc, new KafkaProperties());
		Properties properties = kp.getProperties();
		logger.info("Kafka Properties = " + properties);
		return properties;
	}

}
