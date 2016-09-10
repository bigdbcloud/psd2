package com.ibm.psd2.api.utils.db;

import com.ibm.psd2.commons.datamodel.KafkaProperties;

public interface AppConfigService
{
	public KafkaProperties getKafkaProperties() throws Exception;
}
