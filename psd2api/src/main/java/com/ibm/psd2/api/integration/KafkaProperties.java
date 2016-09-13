package com.ibm.psd2.api.integration;

import java.util.Properties;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "kafkaproperties")
@JsonInclude(value = Include.NON_EMPTY)
public class KafkaProperties
{
	private static final String kafka_bootstrap_servers_key = "bootstrap.servers";
	private static final String kafka_acks_key = "acks";
	private static final String kafka_retries_key = "retries";
	private static final String kafka_batch_size_key = "batch.size";
	private static final String kafka_linger_ms_key = "linger.ms";
	private static final String kafka_buffer_memory_key = "buffer.memory";
	private static final String kafka_key_serializer_key = "key.serializer";
	private static final String kafka_value_serializer_key = "value.serializer";

	private String uuid;

	private String bootstrapServers;

	private String acks;

	private String retries;

	private String batchSize;

	private String lingerMs;

	private String bufferMemory;

	private String keySerializer;

	private String valueSerializer;

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getBootstrapServers()
	{
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers)
	{
		this.bootstrapServers = bootstrapServers;
	}

	public String getAcks()
	{
		return acks;
	}

	public void setAcks(String acks)
	{
		this.acks = acks;
	}

	public String getRetries()
	{
		return retries;
	}

	public void setRetries(String retries)
	{
		this.retries = retries;
	}

	public String getBatchSize()
	{
		return batchSize;
	}

	public void setBatchSize(String batchSize)
	{
		this.batchSize = batchSize;
	}

	public String getLingerMs()
	{
		return lingerMs;
	}

	public void setLingerMs(String lingerMs)
	{
		this.lingerMs = lingerMs;
	}

	public String getBufferMemory()
	{
		return bufferMemory;
	}

	public void setBufferMemory(String bufferMemory)
	{
		this.bufferMemory = bufferMemory;
	}

	public String getKeySerializer()
	{
		return keySerializer;
	}

	public void setKeySerializer(String keySerializer)
	{
		this.keySerializer = keySerializer;
	}

	public String getValueSerializer()
	{
		return valueSerializer;
	}

	public void setValueSerializer(String valueSerializer)
	{
		this.valueSerializer = valueSerializer;
	}

	public Properties getProperties()
	{
		Properties props = new Properties();
		props.setProperty(kafka_bootstrap_servers_key, bootstrapServers);
		props.setProperty(kafka_acks_key, acks);
		props.setProperty(kafka_retries_key, retries);
		props.setProperty(kafka_batch_size_key, batchSize);
		props.setProperty(kafka_linger_ms_key, lingerMs);
		props.setProperty(kafka_buffer_memory_key, bufferMemory);
		props.setProperty(kafka_key_serializer_key, keySerializer);
		props.setProperty(kafka_value_serializer_key, valueSerializer);

		return props;
	}
}
