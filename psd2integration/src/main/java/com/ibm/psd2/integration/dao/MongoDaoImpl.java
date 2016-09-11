package com.ibm.psd2.integration.dao;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.ibm.psd2.integration.ArgumentsContainer;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

public class MongoDaoImpl implements MongoDao
{
	private static final Logger logger = LogManager.getLogger(MongoDaoImpl.class);

	private String collection;
	private String db;
	private ArgumentsContainer ac;
	private MongoDocumentParser mdp;

	public MongoDaoImpl(ArgumentsContainer ac, String collection, String db)
	{
		this.ac = ac;
		this.collection = collection;
		this.db = db;
		this.mdp = new MongoDocumentParser();

		logger.warn("db = " + db);
		logger.warn("collection = " + collection);
	}

	public static String generateUUID()
	{
		return UUID.randomUUID().toString();
	}

	@Override
	public long update(String id, Object idValue, String key, Object value) throws Exception
	{
		MongoDatabase mdb = MongoConnectionFactory.getInstance().getDB(db, ac);
		MongoCollection<Document> coll = mdb.getCollection(collection);

		UpdateResult update = coll.updateOne(new Document(id, idValue),
				new Document("$set", new Document(key, value)));

		return update.getModifiedCount();
	}

	@Override
	public <T> boolean persist(T t) throws Exception
	{
		logger.warn("Persisting Object: " + t);

		if (t == null)
		{
			throw new IllegalArgumentException("Object to persist can't be null");
		}

		MongoDatabase mdb = MongoConnectionFactory.getInstance().getDB(db, ac);
		MongoCollection<Document> coll = mdb.getCollection(collection);
		coll.insertOne(mdp.format(t));

		return true;
	}

	@Override
	public <T> T findOneByAll(Map<String, Object> criteria, T t) throws Exception
	{
		logger.warn("Criteria = " + criteria);
		if (criteria == null || criteria.isEmpty())
		{
			throw new IllegalArgumentException("Search Criteria is null");
		}

		MongoDatabase mdb = MongoConnectionFactory.getInstance().getDB(db, ac);

		MongoCollection<Document> coll = mdb.getCollection(collection);

		Set<Entry<String, Object>> entrySet = criteria.entrySet();

		List<Bson> filters = new ArrayList<Bson>();

		for (Iterator<Entry<String, Object>> iterator = entrySet.iterator(); iterator.hasNext();)
		{
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			filters.add(eq(key, value));
		}


		FindIterable<Document> iterable = coll.find(and(filters)).projection(excludeId());

		if (iterable != null)
		{
			Document document = iterable.first();
			if (document != null)
			{
				logger.warn("Object = " + document.toJson());
				t = mdp.parse(document, t);
			}
			else
			{
				t = null;
			}
		}
		else
		{
			t = null;
		}

		return t;
	}
}
