package com.ibm.api.cashew.beans;

public class Insight
{
	public static final String UNIT_PERCENT = "%";
	public static final String UNIT_VALUE = "";

	String insightId;
	String value;
	String unit;
	String description;

	public String getInsightId()
	{
		return insightId;
	}

	public void setInsightId(String insightId)
	{
		this.insightId = insightId;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public static String getUnitPercent()
	{
		return UNIT_PERCENT;
	}

	public static String getUnitValue()
	{
		return UNIT_VALUE;
	}

}
