package com.ibm.api.cashew.db.elastic;

import java.util.List;

import com.ibm.api.cashew.beans.ElasticTransaction;
import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.beans.aggregation.QueryRequest;

public interface ElasticTransactionCustomRepository
{

	List<AggregationResponse> getBucketAggregation(QueryRequest qr);

	public List<ElasticTransaction> getTransactions(String userId, String bankId, String accountId, String fromDate,
			String toDate);

}
