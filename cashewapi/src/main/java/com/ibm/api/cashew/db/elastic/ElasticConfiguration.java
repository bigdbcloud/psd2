package com.ibm.api.cashew.db.elastic;

import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com/ibm/api/cashew/db/elastic")
public class ElasticConfiguration
{

	private static final Logger logger = LogManager.getLogger(ElasticConfiguration.class);

	@Value("${elasticsearch.host}")
	String host;

	@Value("${elasticsearch.port}")
	int port;

	@Bean
	public Client client()
	{
		TransportClient client = null;
		try
		{
			Settings settings = Settings.settingsBuilder().put("client.transport.ping_timeout", "60s").build();

			System.out.println("Elastic props are: host = " + host + " , port = " + port);
			logger.debug("Elastic props are: host = " + host + " , port = " + port);

			client = TransportClient.builder().settings(settings).build()
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		return client;
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate()
	{
		return new ElasticsearchTemplate(client());
	}
}
