package com.ibm.psd2.integration;

import java.io.Serializable;
import java.util.HashMap;

public class ArgumentsContainer implements Serializable
{
	private HashMap<String, String> argsMap = new HashMap<>();

	public static enum KEYS
	{
		ZOOKEEPER_HOST("zookeeper.host"), KAFKA_TOPIC("kafka.topic"), MONGODB_HOST("mongodb.host"), JOB_SUBMITTER_TYPE(
				"job.submission.type"), MONGODB_DB("psd2api.db"), MONGODB_PSD2_PAYMENTS_COLLECTION(
						"payments.collection"), MONGODB_PSD2_BANKACCOUNTS_COLLECTION(
								"bankaccounts.collection"), MONGODB_PSD2_TRANSACTION_COLLECTION(
										"transactions.collection");

		private String keyValue;

		public String key()
		{
			return keyValue;
		}

		KEYS(String value)
		{
			this.keyValue = value;
		};
	}

	public ArgumentsContainer(String[] args)
	{
		setProperties(args);
	}

	public void setProperties(String[] args)
	{
		System.out.println("Setting properties");
		int i = 0;
		boolean isKey = false;
		String key = null;
		String value = null;
		while (i < args.length)
		{
			if (args[i].indexOf("--") == 0)
			{
				isKey = true;
				key = args[i].substring(2);
			}
			else if (isKey)
			{
				value = args[i];
				argsMap.put(key, value);
				isKey = false;
			}
			i++;
		}
	}

	public String getValue(String key)
	{
		return argsMap.get(key);
	}
}
