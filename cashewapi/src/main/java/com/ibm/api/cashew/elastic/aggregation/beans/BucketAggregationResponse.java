package com.ibm.api.cashew.elastic.aggregation.beans;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public class BucketAggregationResponse extends AggregationResponse
{
	List<BucketResponse> buckets;

	public List<BucketResponse> getBuckets()
	{
		return buckets;
	}

	public void setBuckets(List<BucketResponse> buckets)
	{
		this.buckets = buckets;
	}

	public void addBuckets(BucketResponse bucket)
	{
		if (bucket != null)
		{
			if (buckets == null)
			{
				buckets = new LinkedList<>();
			}

			buckets.add(bucket);
		}
	}
}
