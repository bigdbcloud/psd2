package com.ibm.psd2.integration.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.psd2.integration.ArgumentsContainer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoConnectionFactory
{
	private static final Logger logger = LogManager.getLogger(MongoConnectionFactory.class);

	private static MongoConnectionFactory mcf;
	private MongoClient client = null;

	private MongoConnectionFactory()
	{

	}

	public static synchronized MongoConnectionFactory getInstance()
	{
		if (mcf == null)
		{
			mcf = new MongoConnectionFactory();
		}
		return mcf;
	}

	// private String dbName = "messages";
	//

	private synchronized void createClient(ArgumentsContainer ac)
	{
		if (client == null)
		{
			logger.info("Creating Mongo Client: " + ac.getValue(ArgumentsContainer.KEYS.MONGODB_HOST.key()));
			client = new MongoClient(new MongoClientURI(ac.getValue(ArgumentsContainer.KEYS.MONGODB_HOST.key())));
		}
	}

	public MongoDatabase getDB(String dbName, ArgumentsContainer ac) throws Exception
	{
		if (client == null)
		{
			createClient(ac);
		}

		return client.getDatabase(dbName);
	}
}
