package com.ibm.api.cashew.services;

import java.util.List;

import com.ibm.api.cashew.beans.Insight;

public interface UserInsightsService
{
	public List<Insight> getAvgSpendInAgeGroup(String userId);

}
