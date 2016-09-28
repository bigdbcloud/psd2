package com.ibm.api.cashew.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "goals")
@JsonInclude(value = Include.NON_EMPTY)
public class Goal
{
	public static final String MOOD_POSITIVE = "positive";
	public static final String MOOD_NEGATIVE = "negative";

	@Id
	String id;
	String username;
	String description;
	String threshold;
	String mood;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getThreshold()
	{
		return threshold;
	}

	public void setThreshold(String threshold)
	{
		this.threshold = threshold;
	}

	public String getMood()
	{
		return mood;
	}

	public void setMood(String mood)
	{
		this.mood = mood;
	}
}
