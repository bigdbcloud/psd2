package com.ibm.api.cashew.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.api.cashew.beans.Insight;
import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.beans.aggregation.AggregationRequest;
import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.beans.aggregation.AggregationTypes;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.FieldBean;
import com.ibm.api.cashew.beans.aggregation.MetricAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.QueryRequest;
import com.ibm.api.cashew.db.elastic.ElasticTransactionRepository;

@Service
public class UserInsightsServiceImpl implements UserInsightsService
{
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	public static final long TIME_5_YEARS = 94608000000L;

	private Logger logger = LogManager.getLogger(UserInsightsServiceImpl.class);

	@Autowired
	ElasticTransactionRepository elasticTxnRepo;
	
	@Autowired
	UserService userService;

	@Override
	public List<Insight> getAvgSpendInAgeGroup(String userId)
	{
		logger.debug("Getting Average Spend in age group of user: " + userId);
		User user = userService.findUserById(userId);
		
		if (user == null || user.getDateOfBirth() == null)
		{
			return null;
		}
		
		Insight insight = new Insight();
		
		QueryRequest qr = new QueryRequest();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(user.getDateOfBirth().getTime() - TIME_5_YEARS);
		String fromDate = DATE_FORMAT.format(cal.getTime());
		
		cal = Calendar.getInstance();
		cal.setTimeInMillis(user.getDateOfBirth().getTime() + TIME_5_YEARS);
		String toDate = DATE_FORMAT.format(cal.getTime());

		logger.debug("Age Group Range: " + fromDate + " - " + toDate);
		
		
		qr.setFromDate(fromDate);
		qr.setToDate(toDate);
		qr.setDateField("userInfo.dateOfBirth");

		BucketAggregationRequest aggrBean = new BucketAggregationRequest();
		aggrBean.setName("Age Group Expense distribution by category");
		FieldBean fieldBean = new FieldBean();
		fieldBean.setName("details.tag");
		aggrBean.setField(fieldBean);
		aggrBean.setType(AggregationTypes.TERMS);

		MetricAggregationRequest subAggr = new MetricAggregationRequest();
		subAggr.setName("Average amount");
		FieldBean field = new FieldBean();
		field.setName("details.value.amount");
		subAggr.setField(field);
		subAggr.setType(AggregationTypes.AVG);

		aggrBean.addSubAggregations(subAggr);

		List<AggregationRequest> aggrList = new ArrayList<AggregationRequest>();
		aggrList.add(aggrBean);
		qr.setAggregations(aggrList);
		List<AggregationResponse> aggrResponse = elasticTxnRepo.getBucketAggregation(qr);
		
		logger.debug("Response = " + aggrResponse);
		
		
		return null;
		
		
	}
}
