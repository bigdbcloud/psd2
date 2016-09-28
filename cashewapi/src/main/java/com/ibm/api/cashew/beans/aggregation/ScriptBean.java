package com.ibm.api.cashew.beans.aggregation;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class ScriptBean
{
	String name;
	String scriptText;
	String language;
	private Map<String, Object> params;

	public ScriptBean()
	{

	}

	public ScriptBean(String name, String scriptText, String language)
	{
		this.name = name;
		this.scriptText = scriptText;
		this.language = language;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getScriptText()
	{
		return scriptText;
	}

	public void setScriptText(String scriptText)
	{
		this.scriptText = scriptText;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public Map<String, Object> getParams()
	{
		return params;
	}

	public void setParams(Map<String, Object> params)
	{
		this.params = params;
	}

}
