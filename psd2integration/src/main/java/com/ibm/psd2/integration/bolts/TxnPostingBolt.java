package com.ibm.psd2.integration.bolts;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.commons.beans.aip.BankAccountDetails;
import com.ibm.psd2.commons.beans.aip.TransactionAccount;
import com.ibm.psd2.commons.beans.aip.TransactionBank;
import com.ibm.psd2.commons.beans.aip.Transaction;
import com.ibm.psd2.commons.beans.aip.TransactionDetails;
import com.ibm.psd2.commons.beans.pisp.TxnRequestDetails;
import com.ibm.psd2.integration.ArgumentsContainer;
import com.ibm.psd2.integration.dao.MongoDao;
import com.ibm.psd2.integration.dao.MongoDaoImpl;

public class TxnPostingBolt extends BaseRichBolt
{
	private static final Logger logger = LogManager.getLogger(TxnPostingBolt.class);

	ObjectMapper mapper = null;

	private ArgumentsContainer ac;

	private OutputCollector _collector = null;
	private MongoDao txnDao;
//	private MongoDao bankAccDao;

	public TxnPostingBolt(ArgumentsContainer ac)
	{
		this.ac = ac;
		this.mapper = new ObjectMapper();
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector)
	{
		_collector = collector;

		this.txnDao = new MongoDaoImpl(ac,
				ac.getValue(ArgumentsContainer.KEYS.MONGODB_PSD2_TRANSACTION_COLLECTION.key()),
				ac.getValue(ArgumentsContainer.KEYS.MONGODB_DB.key()));
	}

	@Override
	public void execute(Tuple input)
	{
		logger.warn("Processing tuple: " + input);

		try
		{
			String sourceAccount = (String) input.getValueByField("source");
			String txnRequest = (String)input.getValueByField("txn");
			logger.warn("Processing Txn Request = " + txnRequest);
			
			BankAccountDetails from = mapper.readValue(sourceAccount, BankAccountDetails.class);
			TxnRequestDetails tdb = mapper.readValue(txnRequest, TxnRequestDetails.class);

			Transaction tb = new Transaction();
			
			tb.setId(tdb.getTransaction_ids());
			
			TransactionDetails td = new TransactionDetails();
			td.setCompleted(tdb.getEnd_date());
			td.setDescription(tdb.getBody().getDescription());
			td.setNew_balance(from.getBalance());
			td.setPosted(tdb.getEnd_date());
			td.setType(tdb.getType());
			td.setValue(tdb.getBody().getValue());
			
			tb.setDetails(td);
			
			TransactionAccount thisAcc = new TransactionAccount();
			thisAcc.setId(from.getId());
			
			TransactionBank tbb = new TransactionBank();
			tbb.setName(from.getBank_id());
			tbb.setNational_identifier(from.getBank_id());
			thisAcc.setBank(tbb);
			
			thisAcc.setHolders(from.getOwners());
			thisAcc.setIban(from.getIban());
			thisAcc.setNumber(from.getNumber());
			thisAcc.setSwift_bic(from.getSwift_bic());
			
			tb.setThis_account(thisAcc);
			
			TransactionAccount toAcc = new TransactionAccount();
			toAcc.setId(tdb.getBody().getTo().getAccount_id());
			
			TransactionBank tbb1 = new TransactionBank();
			tbb1.setNational_identifier(tdb.getBody().getTo().getBank_id());
			tbb1.setName(tdb.getBody().getTo().getBank_id());
			toAcc.setBank(tbb1);
			
			tb.setOther_account(toAcc);

			txnDao.persist(tb);
			
			_collector.ack(input);
		}
		catch (Exception e)
		{
			logger.error(e);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
	}

}
