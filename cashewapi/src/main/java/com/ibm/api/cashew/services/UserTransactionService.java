package com.ibm.api.cashew.services;

import java.util.List;

import com.ibm.api.cashew.beans.aggregation.AggregationResponse;

public interface UserTransactionService
{

	public List<AggregationResponse> getUserTxnHistogram(String userId, String bankId, String accountId,
			String fromDate, String toDate);

	public List<AggregationResponse> getUserTxnDistribution(String userId, String bankId, String accountId,
			String txnType, String fromDate, String toDate);

}
