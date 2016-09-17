package com.ibm.api.cashew.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig
{
	@Value("${read.timeout}")
	private int readTimeout;

	@Value("${connect.timeout}")
	private int connectionTimeout;

	@Autowired
	HttpComponentsClientHttpRequestFactoryBasicAuth hcchrf;

	@Bean
	public RestTemplate getRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory)
	{
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		return restTemplate;
	}

	@Bean
	public ClientHttpRequestFactory getClientHttpRequestFactory()
	{

		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpComponentsClientHttpRequestFactory.setConnectTimeout(readTimeout);
		httpComponentsClientHttpRequestFactory.setReadTimeout(connectionTimeout);
		
		return httpComponentsClientHttpRequestFactory;
	}
}