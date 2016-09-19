package com.ibm.api.cashew.elastic.aggregation.beans;

public enum AggregationTypes
{
	SUM("sum"), AVG("avg"), TERMS("terms"), MIN("min"), MAX("max"), COUNT("count"), EXTENDEDSTATS("extendedStats"), DATEHISTOGRAM("dateHistogram"), 
	HISTOGRAM("histogram");

	private String name;

	AggregationTypes(String name)
	{
		this.name = name;
	}

	public String aggregationName()
	{
		return name;
	}
}
