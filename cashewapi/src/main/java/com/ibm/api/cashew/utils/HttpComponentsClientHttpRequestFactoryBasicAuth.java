package com.ibm.api.cashew.utils;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class HttpComponentsClientHttpRequestFactoryBasicAuth extends HttpComponentsClientHttpRequestFactory
{
	String psd2Host;
	String psd2Port;
	
	public HttpComponentsClientHttpRequestFactoryBasicAuth(String host, String port)
	{
		super();
		this.psd2Host = host;
		this.psd2Port = port;
	}

	@Override
	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri)
	{
		if (psd2Host.equals(uri.getHost()))
		{
			AuthCache authCache = new BasicAuthCache();
			BasicScheme basicAuth = new BasicScheme();
			HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort());
			authCache.put(httpHost, basicAuth);
			
			BasicHttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
			return localContext;
		}
		else
		{
			return super.createHttpContext(httpMethod, uri);
		}
	}

}
