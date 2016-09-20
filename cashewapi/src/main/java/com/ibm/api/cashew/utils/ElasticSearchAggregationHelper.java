package com.ibm.api.cashew.utils;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.avg;
import static org.elasticsearch.search.aggregations.AggregationBuilders.count;
import static org.elasticsearch.search.aggregations.AggregationBuilders.dateHistogram;
import static org.elasticsearch.search.aggregations.AggregationBuilders.extendedStats;
import static org.elasticsearch.search.aggregations.AggregationBuilders.histogram;
import static org.elasticsearch.search.aggregations.AggregationBuilders.max;
import static org.elasticsearch.search.aggregations.AggregationBuilders.min;
import static org.elasticsearch.search.aggregations.AggregationBuilders.sum;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import static org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval.DAY;

import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.ibm.api.cashew.beans.aggregation.AggregationRequest;
import com.ibm.api.cashew.beans.aggregation.AggregationTypes;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.BucketAggregationResponse;
import com.ibm.api.cashew.beans.aggregation.BucketResponse;
import com.ibm.api.cashew.beans.aggregation.FieldBean;
import com.ibm.api.cashew.beans.aggregation.MetricAggregationRequest;
import com.ibm.api.cashew.beans.aggregation.MetricAggregationResponse;
import com.ibm.api.cashew.beans.aggregation.QueryRequest;

public class ElasticSearchAggregationHelper {

	private static final Logger logger = LogManager.getLogger(ElasticSearchAggregationHelper.class);
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static QueryBuilder buildQuery(QueryRequest qr) {

		QueryBuilder bqb = null;

		if (qr.getQueryCriteria() != null) {

			bqb = boolQuery();
			for (Iterator<FieldBean> iterator = qr.getQueryCriteria().iterator(); iterator.hasNext();) {
				FieldBean fq = iterator.next();
				MatchQueryBuilder mqb = matchQuery(fq.getName(), fq.getValue());
				((BoolQueryBuilder) bqb).must(mqb);
			}
		}

		if (qr.getFromDate() != null && qr.getToDate() != null && qr.getDateField() != null) {
			DateTime dtFrom = DateTime.parse(qr.getFromDate(), DateTimeFormat.forPattern(DATE_FORMAT.toPattern()));
			DateTime dtTo = DateTime.parse(qr.getToDate(), DateTimeFormat.forPattern(DATE_FORMAT.toPattern()));

			long fromEpoch = dtFrom.getMillis();
			long toEpoch = dtTo.getMillis();

			if (bqb != null && bqb instanceof BoolQueryBuilder) {

				bqb = ((BoolQueryBuilder) bqb)
						.filter(rangeQuery(qr.getDateField()).gte(fromEpoch).lte(toEpoch).format("epoch_millis"));
			} else {

				bqb = rangeQuery(qr.getDateField()).gte(fromEpoch).lte(toEpoch).format("epoch_millis");
			}
		} else{

			bqb = matchAllQuery();
		}
		return bqb;
	}

	public static AbstractAggregationBuilder buildMetricsAggregationRequest(MetricAggregationRequest mar) {
		AbstractAggregationBuilder builder = null;
		if (mar.getType() == AggregationTypes.EXTENDEDSTATS) {
			if (mar.getField() != null) {
				builder = extendedStats(mar.getName()).field(mar.getField().getName());
			} else if (mar.getScript() != null) {
				Script script = new Script(mar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						mar.getScript().getLanguage(), null);
				logger.debug("creating script - " + script);
				builder = extendedStats(mar.getName()).script(script);
			}
		} else if (mar.getType() == AggregationTypes.SUM) {
			if (mar.getField() != null) {
				builder = sum(mar.getName()).field(mar.getField().getName());
			} else if (mar.getScript() != null) {
				Script script = new Script(mar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						mar.getScript().getLanguage(), null);
				logger.debug("creating script - " + script);
				builder = sum(mar.getName()).script(script);
			}
		} else if (mar.getType() == AggregationTypes.AVG) {
			if (mar.getField() != null) {
				builder = avg(mar.getName()).field(mar.getField().getName());
			} else if (mar.getScript() != null) {
				Script script = new Script(mar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						mar.getScript().getLanguage(), null);
				logger.debug("creating script - " + script);
				builder = avg(mar.getName()).script(script);
			}
		} else if (mar.getType() == AggregationTypes.MAX) {
			if (mar.getField() != null) {
				builder = max(mar.getName()).field(mar.getField().getName());
			} else if (mar.getScript() != null) {
				Script script = new Script(mar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						mar.getScript().getLanguage(), null);
				logger.debug("creating script - " + script);
				builder = max(mar.getName()).script(script);
			}
		} else if (mar.getType() == AggregationTypes.MIN) {
			if (mar.getField() != null) {
				builder = min(mar.getName()).field(mar.getField().getName());
			} else if (mar.getScript() != null) {
				Script script = new Script(mar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						mar.getScript().getLanguage(), null);
				logger.debug("creating script - " + script);
				builder = min(mar.getName()).script(script);
			}
		} else if (mar.getType() == AggregationTypes.COUNT) {
			if (mar.getField() != null) {
				builder = count(mar.getName()).field(mar.getField().getName());
			} else if (mar.getScript() != null) {
				Script script = new Script(mar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						mar.getScript().getLanguage(), null);
				logger.debug("creating script - " + script);
				builder = count(mar.getName()).script(script);
			}
		}
		return builder;
	}

	public static AggregationBuilder buildBucketAggregationRequest(QueryRequest qr, BucketAggregationRequest bar) {
		AggregationBuilder builder = null;

		if (qr == null || bar == null) {
			return null;
		}

		DateTime dtFrom = null;
		DateTime dtTo = null;

		if (qr.getDateField() != null || qr.getFromDate() != null || qr.getToDate() != null) {

			dtFrom = DateTime.parse(qr.getFromDate(), DateTimeFormat.forPattern(DATE_FORMAT.toPattern()));
			dtTo = DateTime.parse(qr.getToDate(), DateTimeFormat.forPattern(DATE_FORMAT.toPattern()));
		}

		if (bar.getType() == AggregationTypes.DATEHISTOGRAM) {

			builder = dateHistogram(bar.getName()).field(qr.getDateField())
					.interval(new DateHistogramInterval(bar.getInterval()))
					.timeZone(Calendar.getInstance().getTimeZone().getID()).minDocCount(1).extendedBounds(dtFrom, dtTo);
		} else if (AggregationTypes.TERMS.name().equals(bar.getType().name())) {
			if (bar.getField() != null) {
				builder = terms(bar.getName()).field(bar.getField().getName());
			} else if (bar.getScript() != null) {
				Script script = new Script(bar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						bar.getScript().getLanguage(), bar.getScript().getParams());
				logger.debug("creating script - " + script);
				builder = terms(bar.getName()).script(script);
			}
		} else if (AggregationTypes.HISTOGRAM.name().equals(bar.getType().name())) {
			if (bar.getField() != null) {
				builder = histogram(bar.getName()).field(bar.getField().getName());
			} else if (bar.getScript() != null) {
				Script script = new Script(bar.getScript().getScriptText(), ScriptService.ScriptType.INLINE,
						bar.getScript().getLanguage(), bar.getScript().getParams());
				logger.debug("creating script - " + script);
				builder = histogram(bar.getName()).script(script);
			}
		}

		if (bar.getSubAggregations() != null) {
			for (Iterator<AggregationRequest> iterator = bar.getSubAggregations().iterator(); iterator.hasNext();) {
				AggregationRequest ab = iterator.next();
				logger.debug("setting aggregation - " + ab);
				// TODO: Currently only metric sub aggregation are supported.
				// Need to also support nested aggregations
				builder = builder.subAggregation(buildMetricsAggregationRequest((MetricAggregationRequest) ab));
			}
		}

		return builder;
	}

	public static MetricAggregationResponse buildMetricAggregationResponse(AggregationRequest requestedAggregation,
			Aggregation agg) {
		if (agg == null) {
			return null;
		}

		MetricAggregationResponse responseAggr = new MetricAggregationResponse();

		if (agg instanceof ExtendedStats) {
			ExtendedStats stats = (ExtendedStats) agg;

			responseAggr.setName(requestedAggregation.getName());
			HashMap<String, Object> value = new HashMap<>();

			value.put("min", stats.getMin());
			value.put("max", stats.getMax());

			value.put("avg", stats.getAvg());
			value.put("sum", stats.getSum());
			value.put("count", stats.getCount());
			value.put("stdDeviation", stats.getStdDeviation());
			value.put("sumOfSquares", stats.getSumOfSquares());
			value.put("variance", stats.getVariance());

			responseAggr.setValue(value);
		} else if (agg instanceof Max) {
			Max max = (Max) agg;
			responseAggr.setName(requestedAggregation.getName());
			responseAggr.setValue(max.getValue());
		} else if (agg instanceof Min) {
			Min min = (Min) agg;
			responseAggr.setName(requestedAggregation.getName());
			responseAggr.setValue(min.getValue());
		} else if (agg instanceof Sum) {
			Sum sum = (Sum) agg;
			responseAggr.setName(requestedAggregation.getName());
			responseAggr.setValue(sum.getValue());
		} else if (agg instanceof Avg) {
			Avg avg = (Avg) agg;
			responseAggr.setName(requestedAggregation.getName());
			responseAggr.setValue(avg.getValue());
		} else if (agg instanceof ValueCount) {
			ValueCount count = (ValueCount) agg;
			responseAggr.setName(requestedAggregation.getName());
			responseAggr.setValue(count.getValue());
		}
		return responseAggr;
	}

	public static BucketAggregationResponse buildBucketAggregationResponse(
			BucketAggregationRequest requestedAggregation, Aggregations aggs) {
		if (aggs == null || aggs.get(requestedAggregation.getName()) == null) {
			return null;
		}

		BucketAggregationResponse baresp = new BucketAggregationResponse();

		if (requestedAggregation.getType() == AggregationTypes.DATEHISTOGRAM) {
			InternalHistogram<Bucket> internalHistogram = aggs.get(requestedAggregation.getName());
			List<Bucket> fetchedBuckets = internalHistogram.getBuckets();
			for (Iterator<Bucket> iterator = fetchedBuckets.iterator(); iterator.hasNext();) {
				Bucket fetchedBucket = iterator.next();
				logger.debug("key - " + fetchedBucket.getKeyAsString());

				BucketResponse responseBucket = new BucketResponse();

				responseBucket.setDoc_count(fetchedBucket.getDocCount());
				responseBucket.setKey_as_string(fetchedBucket.getKeyAsString());
				baresp.addBuckets(responseBucket);

				if (requestedAggregation.getSubAggregations() != null) {
					for (Iterator<AggregationRequest> itr = requestedAggregation.getSubAggregations().iterator(); itr
							.hasNext();) {
						AggregationRequest ab = itr.next();
						Aggregation agg = fetchedBucket.getAggregations().get(ab.getName());
						if (agg != null) {
							logger.debug(
									"aggregation name - " + agg.getName() + " value - " + agg.getProperty("value"));
							MetricAggregationResponse mar = buildMetricAggregationResponse(ab, agg);
							responseBucket.addAggregations(mar);
						} else {
							logger.warn("Aggregation not found - " + ab.getName());
						}
					}
				}
			}
		} else if (AggregationTypes.TERMS == requestedAggregation.getType()) {

			Object obj = aggs.get(requestedAggregation.getName());

			if (obj instanceof StringTerms) {
				baresp.setName(requestedAggregation.getName());

				StringTerms stringTerm = (StringTerms) obj;
				List<org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket> buckets = stringTerm.getBuckets();
				for (Iterator<org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket> iterator = buckets
						.iterator(); iterator.hasNext();) {

					org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket = iterator.next();
					BucketResponse aggrBucket = new BucketResponse();
					aggrBucket.setKey(bucket.getKey());
					aggrBucket.setDoc_count(bucket.getDocCount());
					baresp.addBuckets(aggrBucket);
					

					if (requestedAggregation.getSubAggregations() != null) {
						for (Iterator<AggregationRequest> itr = requestedAggregation.getSubAggregations().iterator(); itr
								.hasNext();) {
							AggregationRequest ab = itr.next();
							Aggregation agg = bucket.getAggregations().get(ab.getName());
							if (agg != null) {
								logger.debug(
										"aggregation name - " + agg.getName() + " value - " + agg.getProperty("value"));
								MetricAggregationResponse mar = buildMetricAggregationResponse(ab, agg);
								aggrBucket.addAggregations(mar);
							} else {
								logger.warn("Aggregation not found - " + ab.getName());
							}
						}
					}
				}

			} else if (obj instanceof LongTerms) {

				baresp.setName(requestedAggregation.getName());

				LongTerms longTerms = (LongTerms) obj;
				List<org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket> buckets = longTerms.getBuckets();
				for (Iterator<org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket> iterator = buckets
						.iterator(); iterator.hasNext();) {

					org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket = iterator.next();
					BucketResponse aggrBucket = new BucketResponse();
					aggrBucket.setKey(bucket.getKey());
					aggrBucket.setDoc_count(bucket.getDocCount());
					baresp.addBuckets(aggrBucket);
				}
			}
		}
		return baresp;
	}

	public static QueryRequest buildQueryRequest(List<AggregationRequest> aggReq, String fromDate, String toDate,
			String dataField) {

		QueryRequest qr = new QueryRequest();

		qr.setFromDate(fromDate);
		qr.setToDate(toDate);
		qr.setDateField(dataField);

		qr.setAggregations(aggReq);

		return qr;
	}

}
