package com.ibm.psd2.integration.bolts;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.commons.beans.aip.BankAccountDetails;
import com.ibm.psd2.commons.beans.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.utils.UUIDGenerator;
import com.ibm.psd2.integration.ArgumentsContainer;
import com.ibm.psd2.integration.dao.MongoDao;
import com.ibm.psd2.integration.dao.MongoDaoImpl;

public class TxnRequestProcessor extends BaseRichBolt
{
	private static final Logger logger = LogManager.getLogger(TxnRequestProcessor.class);

	ObjectMapper mapper = null;

	private ArgumentsContainer ac;

	private OutputCollector _collector = null;
	private MongoDao paymentDao;
	private MongoDao bankAccDao;

	public TxnRequestProcessor(ArgumentsContainer ac)
	{
		this.ac = ac;
		this.mapper = new ObjectMapper();
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
	{
		_collector = collector;

		this.paymentDao = new MongoDaoImpl(ac,
				ac.getValue(ArgumentsContainer.KEYS.MONGODB_PSD2_PAYMENTS_COLLECTION.key()),
				ac.getValue(ArgumentsContainer.KEYS.MONGODB_DB.key()));
		this.bankAccDao = new MongoDaoImpl(ac,
				ac.getValue(ArgumentsContainer.KEYS.MONGODB_PSD2_BANKACCOUNTS_COLLECTION.key()),
				ac.getValue(ArgumentsContainer.KEYS.MONGODB_DB.key()));
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

			BankAccountDetails from = new BankAccountDetails();
			BankAccountDetails to = new BankAccountDetails();

			Map<String, Object> criteriaFrom = new HashMap<>();
			Map<String, Object> criteriaTo = new HashMap<>();

			criteriaFrom.put("id", tdb.getFrom().getAccount_id());
			criteriaTo.put("id", tdb.getBody().getTo().getAccount_id());

			from = bankAccDao.findOneByAll(criteriaFrom, from);
			to = bankAccDao.findOneByAll(criteriaTo, to);

			if (from != null)
			{
				double balance = from.getBalance().getAmount();
				double amount = tdb.getBody().getValue().getAmount();
				double charge = tdb.getCharge().getValue().getAmount();
				balance = balance - amount - charge;
				from.getBalance().setAmount(balance);
				bankAccDao.update("id", from.getId(), "balance.amount", balance);
			}

			if (to != null)
			{
				double balance = to.getBalance().getAmount();
				double amount = tdb.getBody().getValue().getAmount();
				balance = balance + amount;
				to.getBalance().setAmount(balance);
				bankAccDao.update("id", to.getId(), "balance.amount", balance);
			}

			tdb.setStatus(TxnRequestDetails.TXN_STATUS_COMPLETED);
			tdb.setEnd_date(new Date());
			tdb.setTransaction_ids(UUIDGenerator.generateUUID());

			paymentDao.update("id", tdb.getId(), "status", tdb.getStatus());
			paymentDao.update("id", tdb.getId(), "end_date", tdb.getEnd_date());
			paymentDao.update("id", tdb.getId(), "transaction_ids", UUIDGenerator.generateUUID());

			_collector.emit(input, new Values(tdb.getId(), mapper.writeValueAsString(from),
					mapper.writeValueAsString(to), mapper.writeValueAsString(tdb)));
			_collector.ack(input);

		} catch (Exception e)
		{
			logger.error(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		declarer.declare(new Fields("uuid", "source", "to", "txn"));
	}

}
