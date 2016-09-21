package com.ibm.api.cashew.db.elastic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.ibm.api.cashew.beans.aggregation.AggregationRequest;
import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationResponse;
import com.ibm.api.cashew.beans.aggregation.BucketResponse;
import com.ibm.api.cashew.beans.aggregation.QueryRequest;
import com.ibm.api.cashew.utils.ElasticSearchAggregationHelper;

public class ElasticTransactionRepositoryImpl implements ElasticTransactionCustomRepository
{

	private static final Logger logger = LogManager.getLogger(ElasticTransactionRepositoryImpl.class);

	@Autowired
	ElasticsearchOperations elasticTemplate;

	@Override
	public List<AggregationResponse> getBucketAggregation(QueryRequest qr)
	{

		if (qr == null || qr.getAggregations() == null || qr.getAggregations().isEmpty())
		{
			return null;
		}

		QueryBuilder qb = ElasticSearchAggregationHelper.buildQuery(qr);

		NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder().withQuery(qb)
				.withSearchType(SearchType.QUERY_THEN_FETCH).withIndices("transactions").withTypes("transaction");

		for (Iterator<AggregationRequest> iterator = qr.getAggregations().iterator(); iterator.hasNext();)
		{
			AggregationRequest bar = iterator.next();
			if (bar instanceof BucketAggregationRequest)
			{
				AbstractAggregationBuilder builder = ElasticSearchAggregationHelper.buildBucketAggregationRequest(qr,
						(BucketAggregationRequest) bar);
				if (builder != null)
				{
					searchQuery.addAggregation(builder);
				}
			}
		}

		Aggregations aggregations = elasticTemplate.query(searchQuery.build(), new ResultsExtractor<Aggregations>()
		{
			@Override
			public Aggregations extract(SearchResponse response)
			{
				return response.getAggregations();
			}
		});

		List<AggregationResponse> response = new ArrayList<>();
		for (Iterator<AggregationRequest> iterator = qr.getAggregations().iterator(); iterator.hasNext();)
		{
			BucketAggregationRequest bareq = (BucketAggregationRequest) iterator.next();
			BucketAggregationResponse baresp = ElasticSearchAggregationHelper.buildBucketAggregationResponse(bareq,
					aggregations);
			if (baresp != null)
			{
				response.add(baresp);
			}

		}

		return response;
	}
}
