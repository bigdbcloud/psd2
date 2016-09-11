package com.ibm.psd2.integration.bolts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;


public class MessageLoggingBolt extends BaseBasicBolt
{
	private static final Logger logger = LogManager.getLogger(MessageLoggingBolt.class);
	
	public void execute(Tuple input, BasicOutputCollector collector)
	{
		logger.warn("Received Message from Kafka = " + input);
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		// TODO Auto-generated method stub
	}
	
	
	
}
