package com.ibm.psd2.integration.jobs;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import com.ibm.psd2.integration.ArgumentsContainer;
import com.ibm.psd2.integration.bolts.MessageLoggingBolt;
import com.ibm.psd2.integration.bolts.TxnPostingBolt;
import com.ibm.psd2.integration.bolts.TxnRequestProcessor;

public class EventProcessTopology
{
	private static final Logger logger = LogManager.getLogger(StormJobSubmitter.class);

	private static final String kafkaSpoutId = "KafkaSpout";
	private static final String messageLogger = "LogMessageBolt";
	private static final String txnRequestProcessor = "TransactionRequestProcessingBolt";
	private static final String txnPublisher = "TransactionPublishingBolt";

	public EventProcessTopology(ArgumentsContainer ac)
	{
		this.ac = ac;
	}

	ArgumentsContainer ac;

	public StormTopology createTopology()
	{
		String zkHost = ac.getValue(ArgumentsContainer.KEYS.ZOOKEEPER_HOST.key());
		String kafkaTopic = ac.getValue(ArgumentsContainer.KEYS.KAFKA_TOPIC.key());

		logger.info("zkHost = " + zkHost);
		logger.info("kafkaTopic = " + kafkaTopic);

		BrokerHosts hosts = new ZkHosts(zkHost);
		SpoutConfig spoutConfig = new SpoutConfig(hosts, kafkaTopic, "/kafkastorm", UUID.randomUUID().toString());
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout(kafkaSpoutId, kafkaSpout, 1);
		builder.setBolt(txnRequestProcessor, new TxnRequestProcessor(ac), 1).shuffleGrouping(kafkaSpoutId);
		builder.setBolt(messageLogger, new MessageLoggingBolt(), 1).shuffleGrouping(kafkaSpoutId);
		builder.setBolt(txnPublisher, new TxnPostingBolt(ac), 1).shuffleGrouping(txnRequestProcessor);

		return builder.createTopology();
	}
}
