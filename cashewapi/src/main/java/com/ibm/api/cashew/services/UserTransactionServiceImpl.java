package com.ibm.api.cashew.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.api.cashew.beans.aggregation.AggregationRequest;
import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.beans.aggregation.AggregationTypes;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.FieldBean;
import com.ibm.api.cashew.beans.aggregation.MetricAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.QueryRequest;
import com.ibm.api.cashew.db.elastic.ElasticTransactionRepository;

@Service
public class UserTransactionServiceImpl implements UserTransactionService {

	@Autowired
	ElasticTransactionRepository elasticTxnRepo;
	
	@Override
	public List<AggregationResponse> getUserTxnDistribution(String userId, String bankId, String accountId) {
		

		QueryRequest qr= buildQueryForTxnDistribution(userId,bankId,accountId);
		return elasticTxnRepo.getBucketAggregation(qr);
		
	}
	
	@Override
	public List<AggregationResponse> getUserAvgTxnDistribution(String userId, String bankId, String accountId,String fromDate, String toDate) {
		

		QueryRequest qr= buildQueryForAvgTxnDistribution(userId,bankId,accountId,fromDate,toDate);
		return elasticTxnRepo.getBucketAggregation(qr);
		
	}
	

	private QueryRequest buildQueryForAvgTxnDistribution(String userId, String bankId, String accountId,String fromDate, String toDate)
	{
		QueryRequest qr = new QueryRequest();

		qr.setFromDate(fromDate);
		qr.setToDate(toDate);
		qr.setDateField("details.posted");

		if (bankId != null)
		{
			qr.addQueryCriteria(new FieldBean("from.bankId", bankId));
		}

		if (accountId != null)
		{
			qr.addQueryCriteria(new FieldBean("from.accountId", accountId));
		}

		BucketAggregationRequest aggrBean = new BucketAggregationRequest();
		aggrBean.setName("User avg expenses");
		aggrBean.setInterval("1d");
			
		aggrBean.setType(AggregationTypes.DATEHISTOGRAM);
		
		MetricAggregationRequest subAggr=new MetricAggregationRequest();
		subAggr.setName("Total amount");
		FieldBean field = new FieldBean();
		field.setName("details.value.amount");
		subAggr.setField(field);
		
		subAggr.setType(AggregationTypes.AVG);
		
		aggrBean.addSubAggregations(subAggr);;

		List<AggregationRequest> aggrList = new ArrayList<AggregationRequest>();
		aggrList.add(aggrBean);
		qr.setAggregations(aggrList);
		
		return qr;
	}

	private QueryRequest buildQueryForTxnDistribution(String userId, String bankId, String accountId)
	{
		QueryRequest qr = new QueryRequest();


		if (bankId != null)
		{
			qr.addQueryCriteria(new FieldBean("from.bankId", bankId));
		}

		if (accountId != null)
		{
			qr.addQueryCriteria(new FieldBean("from.accountId", accountId));
		}

		BucketAggregationRequest aggrBean = new BucketAggregationRequest();
		aggrBean.setName("User expense distribution by category");
		FieldBean fieldBean = new FieldBean();		
		fieldBean.setName("details.tag");
		aggrBean.setField(fieldBean);
		aggrBean.setType(AggregationTypes.TERMS);

		
		MetricAggregationRequest subAggr=new MetricAggregationRequest();
		subAggr.setName("Total amount");
		FieldBean field = new FieldBean();
		field.setName("details.value.amount");
		subAggr.setField(field);
		subAggr.setType(AggregationTypes.SUM);
	
		
		aggrBean.addSubAggregations(subAggr);;

		List<AggregationRequest> aggrList = new ArrayList<AggregationRequest>();
		aggrList.add(aggrBean);
		qr.setAggregations(aggrList);
		
		return qr;
	}

}