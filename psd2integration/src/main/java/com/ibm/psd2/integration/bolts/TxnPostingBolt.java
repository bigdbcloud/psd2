package com.ibm.psd2.integration.bolts;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.aip.TransactionAccount;
import com.ibm.psd2.datamodel.aip.TransactionBank;
import com.ibm.psd2.datamodel.aip.TransactionDetails;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.integration.ArgumentsContainer;
import com.ibm.psd2.integration.dao.MongoConfig;

public class TxnPostingBolt extends BaseRichBolt
{
	private static final Logger logger = LogManager.getLogger(TxnPostingBolt.class);

	ObjectMapper mapper = null;

	private ArgumentsContainer ac;

	private OutputCollector _collector = null;
	// private MongoDao txnDao;

	MongoOperations mongoOperation = null;

	public TxnPostingBolt(ArgumentsContainer ac)
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
			String sourceAccount = (String) input.getValueByField("source");
			String txnRequest = (String) input.getValueByField("txn");
			logger.warn("Processing Txn Request = " + txnRequest);

			BankAccountDetails from = mapper.readValue(sourceAccount, BankAccountDetails.class);
			TxnRequestDetails txnRequestDetails = mapper.readValue(txnRequest, TxnRequestDetails.class);

			Transaction txn = new Transaction();

			txn.setId(txnRequestDetails.getTransactionIds());

			TransactionDetails txnDetails = new TransactionDetails();
			txnDetails.setCompleted(Transaction.DATE_FORMAT.parse(txnRequestDetails.getEndDate()));
			txnDetails.setDescription(txnRequestDetails.getBody().getDescription());
			txnDetails.setNewBalance(from.getBalance());
			txnDetails.setPosted(Transaction.DATE_FORMAT.parse(txnRequestDetails.getEndDate()));
			txnDetails.setType(txnRequestDetails.getType());
			txnDetails.setValue(txnRequestDetails.getBody().getValue());

			txn.setDetails(txnDetails);

			TransactionAccount thisAcc = new TransactionAccount();
			thisAcc.setId(from.getId());

			TransactionBank tbb = new TransactionBank();
			tbb.setName(from.getBankId());
			tbb.setNationalIdentifier(from.getBankId());
			thisAcc.setBank(tbb);

			thisAcc.setHolders(from.getOwners());
			thisAcc.setIban(from.getIban());
			thisAcc.setNumber(from.getNumber());
			thisAcc.setSwiftBic(from.getSwiftBic());

			txn.setThisAccount(thisAcc);

			TransactionAccount toAcc = new TransactionAccount();
			toAcc.setId(txnRequestDetails.getBody().getTo().getAccountId());

			TransactionBank tbb1 = new TransactionBank();
			tbb1.setNationalIdentifier(txnRequestDetails.getBody().getTo().getBankId());
			tbb1.setName(txnRequestDetails.getBody().getTo().getBankId());
			toAcc.setBank(tbb1);

			txn.setOtherAccount(toAcc);

			logger.warn("Saving Transaction = " + txn);

			mongoOperation.save(txn);

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
	}

}
