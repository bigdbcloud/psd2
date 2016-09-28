package com.ibm.api.cashew.beans.aggregation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_EMPTY)
public class QueryRequest
{
	List<FieldBean> queryCriteria;
	String dateField;
	String timeZone;
	String fromDate;
	String toDate;
	List<AggregationRequest> aggregations;

	Integer page;
	Integer limit;

	public String getDateField()
	{
		return dateField;
	}

	public void setDateField(String dateField)
	{
		this.dateField = dateField;
	}

	public String getTimeZone()
	{
		return timeZone;
	}

	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}

	public String getFromDate()
	{
		return fromDate;
	}

	public void setFromDate(String fromDate)
	{
		this.fromDate = fromDate;
	}

	public String getToDate()
	{
		return toDate;
	}

	public void setToDate(String toDate)
	{
		this.toDate = toDate;
	}

	public List<FieldBean> getQueryCriteria()
	{
		return queryCriteria;
	}

	public void setQueryCriteria(List<FieldBean> mustCriteria)
	{
		this.queryCriteria = mustCriteria;
	}

	public void addQueryCriteria(FieldBean query)
	{
		if (queryCriteria == null)
		{
			queryCriteria = new LinkedList<>();
		}
		queryCriteria.add(query);
	}

	public void addAggregations(AggregationRequest aggregationRequest)
	{
		if (aggregations == null)
		{
			aggregations = new ArrayList<>();
		}
		aggregations.add(aggregationRequest);
	}

	public List<AggregationRequest> getAggregations()
	{
		return aggregations;
	}

	public void setAggregations(List<AggregationRequest> aggregations)
	{
		this.aggregations = aggregations;
	}

	public Integer getPage()
	{
		return page;
	}

	public void setPage(Integer page)
	{
		this.page = page;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public void setLimit(Integer limit)
	{
		this.limit = limit;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
