package com.ibm.psd2.api.aip.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoBankRepository;
import com.ibm.psd2.commons.datamodel.Bank;

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
	public Bank getBankDetails(String bankId) throws Exception
	{
		logger.info("bankId = " + bankId);
		return mbr.findOne(bankId);
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(banks);
//		FindIterable<Document> iterable = coll.find(eq("id", bankId)).projection(excludeId());
//		Bank b = null;
//
//		for (Document document : iterable)
//		{
//			if (document != null)
//			{
//				logger.info("message = " + document.toJson());
//				b = mdp.parse(document, new Bank());
//			}
//		}
//		return b;
	}

	@Override
	public List<Bank> getBanks() throws Exception
	{
		return mbr.findAll();
		
//		MongoCollection<Document> coll = conn.getDB().getCollection(banks);
//		FindIterable<Document> iterable = coll.find().projection(excludeId());
//		ArrayList<Bank> b = null;
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
//				b.add(mdp.parse(document, new Bank()));
//			}
//		}
//		return b;
	}

	@Override
	public Bank createBank(Bank b) throws Exception
	{
		return mbr.save(b);
//		MongoCollection<Document> coll = conn.getDB().getCollection(banks);
//		coll.insertOne(mdp.format(b));
//
//		return b;
	}
}
