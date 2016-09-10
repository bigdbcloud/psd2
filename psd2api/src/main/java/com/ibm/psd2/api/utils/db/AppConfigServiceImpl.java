package com.ibm.psd2.api.utils.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.api.utils.KafkaProperties;

@Service
public class AppConfigServiceImpl implements AppConfigService
{
	private Logger logger = LogManager.getLogger(AppConfigServiceImpl.class);

	@Autowired
	private MongoKafkaPropertiesRepository macr;

	@Override
	public KafkaProperties getKafkaProperties() throws Exception
	{
		logger.debug("getKafkaProperties invoked");
		return macr.findByUuid(Constants.APP_CONFIG_KAFKA_PROPERTIES);
	}

}
