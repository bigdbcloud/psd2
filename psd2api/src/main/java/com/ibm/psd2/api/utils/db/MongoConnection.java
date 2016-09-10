package com.ibm.psd2.api.utils.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


@Component
public class MongoConnection
{
	private MongoClient client = null;

	@Value("${mongodb.url}")
	private String mongoURL;

	@Value("${mongodb.db.psd2api}")
	private String dbName = null;

	private static final Logger logger = LogManager.getLogger(MongoConnection.class);

	public MongoConnection()
	{
	}

	private synchronized void createClient()
	{
		if (client == null)
		{
			logger.info("Creating Mongo Client: " + mongoURL);
			client = new MongoClient(new MongoClientURI(mongoURL));
		}
	}

	public MongoDatabase getDB() throws Exception
	{
		createClient();
		return client.getDatabase(dbName);
	}
}
