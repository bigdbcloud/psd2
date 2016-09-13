package com.ibm.psd2.api.integration;

import java.util.Calendar;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.utils.Constants;

@Component
public class KafkaMessageProducer
{
	private final Logger logger = LogManager.getLogger(KafkaMessageProducer.class);

	@Value("${psd2api.integration.kafka.refresh.interval.ms}")
	private long reloadTime;

	private long lastLoadTime = 0;

	private KafkaProducer<String, String> producer;

	@Autowired
	private MongoKafkaPropertiesRepository mkpr;

	private synchronized KafkaProducer<String, String> getProducer() throws Exception
	{
		long loadInterval = Calendar.getInstance().getTimeInMillis() - lastLoadTime;
		if (producer == null || lastLoadTime == 0 || loadInterval > reloadTime)
		{
			logger.info("Creating Kafka Producer... " + "Load Interval = " + loadInterval);
			KafkaProperties kp = mkpr.findByUuid(Constants.APP_CONFIG_KAFKA_PROPERTIES);
			Properties props = kp.getProperties();
			producer = new KafkaProducer<>(props);
			lastLoadTime = Calendar.getInstance().getTimeInMillis();
			logger.info("Creating Kafka Producer complete...");
		}
		return producer;
	}

	public void publishMessage(String topic, String key, String value) throws Exception
	{
		logger.info("Sending Kafka Message on topic: " + topic + " with key: " + key + " message: " + value);
		getProducer().send(new ProducerRecord<String, String>(topic, key, value));
		logger.info("Sending Kafka Message on topic: " + topic + " with key: " + key + " complete...");
	}

}
