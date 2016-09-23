package com.ibm.api.cashew.beans.aggregation;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
