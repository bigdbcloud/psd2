package com.ibm.psd2.integration.bolts;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.integration.ArgumentsContainer;
import com.ibm.psd2.integration.dao.MongoConfig;
import com.ibm.psd2.utils.UUIDGenerator;

public class TxnRequestProcessor extends BaseRichBolt
{
	private static final Logger logger = LogManager.getLogger(TxnRequestProcessor.class);

	ObjectMapper mapper = null;

	private ArgumentsContainer ac;

	private OutputCollector _collector = null;

	MongoOperations mongoOperation;

	public TxnRequestProcessor(ArgumentsContainer ac)
	{
		this.ac = ac;
		this.mapper = new ObjectMapper();
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
	{
		_collector = collector;

		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
	}

	@Override
	public void execute(Tuple input)
	{
		logger.warn("Processing tuple: " + input);

		try
		{
			String transaction = input.getString(0);
			logger.warn("Parsing Transaction: " + transaction);

			TxnRequestDetails tdb = mapper.readValue(transaction, TxnRequestDetails.class);

			logger.warn("Processing Transaction Request:" + tdb.getId());

			BankAccountDetails from = mongoOperation.findOne(query(where("id").is(tdb.getFrom().getAccountId())),
					BankAccountDetails.class);
			BankAccountDetails to = mongoOperation.findOne(query(where("id").is(tdb.getBody().getTo().getAccountId())),
					BankAccountDetails.class);

			if (from != null)
			{
				double balance = from.getBalance().getAmount();
				double amount = tdb.getBody().getValue().getAmount();
				double charge = tdb.getCharge().getValue().getAmount();
				balance = balance - amount - charge;
				from.getBalance().setAmount(balance);
				mongoOperation.updateFirst(query(where("id").is(from.getId())), update("balance.amount", balance),
						BankAccountDetails.class);
			}

			if (to != null)
			{
				double balance = to.getBalance().getAmount();
				double amount = tdb.getBody().getValue().getAmount();
				balance = balance + amount;
				to.getBalance().setAmount(balance);
				mongoOperation.updateFirst(query(where("id").is(to.getId())), update("balance.amount", balance),
						BankAccountDetails.class);
			}

			tdb.setStatus(TxnRequestDetails.TXN_STATUS_COMPLETED);
			tdb.setEndDate(new Date());
			tdb.setTransactionIds(UUIDGenerator.generateUUID());

			mongoOperation.save(tdb);
			_collector.emit(input, new Values(tdb.getId(), mapper.writeValueAsString(from),
					mapper.writeValueAsString(to), mapper.writeValueAsString(tdb)));
			_collector.ack(input);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		declarer.declare(new Fields("uuid", "source", "to", "txn"));
	}

}
