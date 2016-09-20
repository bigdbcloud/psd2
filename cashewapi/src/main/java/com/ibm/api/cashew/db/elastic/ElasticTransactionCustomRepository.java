package com.ibm.api.cashew.db.elastic;

import java.util.List;

import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.beans.aggregation.QueryRequest;

public interface ElasticTransactionCustomRepository {

	List<AggregationResponse> getBucketAggregation(QueryRequest qr);

}
