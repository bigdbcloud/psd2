package com.ibm.api.cashew.beans.aggregation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class BucketAggregationRequest extends AggregationRequest
{

	String interval;
	int minDocCount;
	private List<AggregationRequest> subAggregations;

	public String getInterval()
	{
		return interval;
	}

	public void setInterval(String interval)
	{
		this.interval = interval;
	}

	public int getMinDocCount()
	{
		return minDocCount;
	}

	public void setMinDocCount(int minDocCount)
	{
		this.minDocCount = minDocCount;
	}

	public List<AggregationRequest> getSubAggregations()
	{
		return subAggregations;
	}

	public void setSubAggregations(List<AggregationRequest> subaggregations)
	{
		this.subAggregations = subaggregations;
	}

	public void addSubAggregations(AggregationRequest ab)
	{
		if (subAggregations == null)
		{
			subAggregations = new ArrayList<>();
		}
		subAggregations.add(ab);
	}

}
