package com.ibm.api.cashew.services;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ibm.api.cashew.beans.ElasticTransaction;
import com.ibm.api.cashew.beans.Insight;
import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.beans.aggregation.AggregationRequest;
import com.ibm.api.cashew.beans.aggregation.AggregationResponse;
import com.ibm.api.cashew.beans.aggregation.AggregationTypes;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationResponse;
import com.ibm.api.cashew.beans.aggregation.BucketResponse;
import com.ibm.api.cashew.beans.aggregation.FieldBean;
import com.ibm.api.cashew.beans.aggregation.MetricAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.MetricAggregationResponse;
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

	private static String EXPENSE_INSIGHT_MSG = "Users in your age group spend {0} GBP on {1}";
	private static String INCOME_INSIGHT_MSG = "Users in your age group earn {0} GBP through {1}";

	@Override
	public List<Insight> getAvgSpendInAgeGroup(String userId, String txnType)
	{
		logger.debug("Fetching Average Spend in age group of user: " + userId);
		User user = userService.findUserById(userId);

		if (user == null || user.getDateOfBirth() == null)
		{
			return null;
		}

		Date date = null;

		try
		{
			date = DATE_FORMAT.parse(user.getDateOfBirth());
		}
		catch (ParseException e)
		{
			logger.error(e.getMessage(), e);
			return null;
		}

		Insight insight = new Insight();
		QueryRequest qr = new QueryRequest();
		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(date.getTime() - TIME_5_YEARS);
		String fromDate = DATE_FORMAT.format(cal.getTime());

		cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime() + TIME_5_YEARS);
		String toDate = DATE_FORMAT.format(cal.getTime());

		logger.debug("Age Group Range: " + fromDate + " - " + toDate);

		qr.setFromDate(fromDate);
		qr.setToDate(toDate);
		qr.setDateField("userInfo.dateOfBirth");

		if (txnType != null && ElasticTransaction.TXN_TYPE_CREDIT.equals(txnType))
		{
			qr.addQueryCriteria(new FieldBean("txnType", ElasticTransaction.TXN_TYPE_CREDIT));
		}
		else
		{
			qr.addQueryCriteria(new FieldBean("txnType", ElasticTransaction.TXN_TYPE_DEBIT));
		}

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

		List<Insight> insights = null;

		if (!CollectionUtils.isEmpty(aggrResponse))
		{

			insights = new ArrayList<Insight>();
			for (AggregationResponse aggrRes : aggrResponse)
			{

				if (aggrRes != null && (aggrRes instanceof BucketAggregationResponse))
				{

					BucketAggregationResponse res = (BucketAggregationResponse) aggrRes;
					List<BucketResponse> buckets = res.getBuckets();

					for (BucketResponse bucketRes : buckets)
					{

						List<AggregationResponse> resList = bucketRes.getAggregations();

						for (AggregationResponse subAgg : resList)
						{

							if (subAgg != null && (subAgg instanceof MetricAggregationResponse))
							{

								MetricAggregationResponse metricRes = (MetricAggregationResponse) subAgg;

								Insight insght = new Insight();

								if (txnType != null && ElasticTransaction.TXN_TYPE_CREDIT.equals(txnType))
								{
									insght.setDescription(MessageFormat.format(INCOME_INSIGHT_MSG,
											((Double) metricRes.getValue()).toString().replaceAll("-", ""),
											bucketRes.getKey_as_string()));
								}
								else
								{
									insght.setDescription(MessageFormat.format(EXPENSE_INSIGHT_MSG,
											((Double) metricRes.getValue()).toString().replaceAll("-", ""),
											bucketRes.getKey_as_string()));
								}
								insght.setUnit("AVG");
								insght.setValue(((Double) metricRes.getValue()).toString().replaceAll("-", ""));
								insights.add(insght);
							}
						}

					}
				}

			}
		}

		return insights;

	}

}
