package com.ibm.api.cashew.dashboard.services;

import java.util.List;

import com.ibm.api.cashew.beans.aggregation.AggregationResponse;

public interface UserTransactionService {

	public List<AggregationResponse> getUserTxnDistribution(String userId, String bankId, String accountId);
		

}
