package com.ibm.psd2.api.utils;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "AppConfig")
@JsonInclude(value = Include.NON_EMPTY)
public class AppConfig implements Serializable
{
	@Id
	String uuid;
	Map<String, String> properties;

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public Map<String, String> getProperties()
	{
		return properties;
	}

	public void setProperties(Map<String, String> properties)
	{
		this.properties = properties;
	}

}
