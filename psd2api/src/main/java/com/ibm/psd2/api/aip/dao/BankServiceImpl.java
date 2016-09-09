package com.ibm.psd2.api.aip.dao;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoBankRepository;
import com.ibm.psd2.api.common.db.MongoConnection;
import com.ibm.psd2.api.common.db.MongoDocumentParser;
import com.ibm.psd2.commons.beans.BankBean;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

@Service
public class BankServiceImpl implements BankService
{

	private final Logger logger = LogManager.getLogger(BankServiceImpl.class);

//	@Autowired
//	private MongoConnection conn;
//
//	@Autowired
//	private MongoDocumentParser mdp;
//
//	@Value("${mongodb.collection.banks}")
//	private String banks;
	
	@Autowired
	private MongoBankRepository mbr;

	@Override
	public BankBean getBankDetails(String bankId) throws Exception
	{
		logger.info("bankId = " + bankId);
		return mbr.findOne(bankId);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(banks);
//		FindIterable<Document> iterable = coll.find(eq("id", bankId)).projection(excludeId());
//		BankBean b = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				logger.info("message = " + document.toJson());
//				b = mdp.parse(document, new BankBean());
//			}
//		}
//		return b;
	}

	@Override
	public List<BankBean> getBanks() throws Exception
	{
		return mbr.findAll();
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(banks);
//		FindIterable<Document> iterable = coll.find().projection(excludeId());
//		ArrayList<BankBean> b = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				if (b == null)
//				{
//					b = new ArrayList<>();
//				}
//				logger.info("message = " + document.toJson());
//				b.add(mdp.parse(document, new BankBean()));
//			}
//		}
//		return b;
	}

	@Override
	public BankBean createBank(BankBean b) throws Exception
	{
		return mbr.save(b);
//		MongoCollection<Document> coll = conn.getDB().getCollection(banks);
//		coll.insertOne(mdp.format(b));
//
//		return b;
	}
}
