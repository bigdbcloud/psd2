package com.ibm.psd2.api.aip.db;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.datamodel.aip.Transaction;

public class MongoTransactionsRepositoryImpl implements MongoTransactionsRepositoryCustom
{
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Transaction> getTransactions(String bankId, String accountId, String sortDirection, Date fromDate,
			Date toDate, String sortBy, Integer page, Integer limit)
	{

		Sort sort = null;

		if (sortDirection != null && Constants.SORT_ASCENDING.equalsIgnoreCase(sortDirection))
		{
			sort = new Sort(Sort.Direction.ASC, "details.posted");
		}
		else
		{
			sort = new Sort(Sort.Direction.DESC, "details.posted");
		}

		PageRequest pager = null;

		if (page != null && limit != null)
		{
			pager = new PageRequest(page, limit, sort);
		}

		CriteriaDefinition cd = null;

		if (fromDate != null)
		{
			if (toDate == null)
			{
				toDate = new Date();
			}
			cd = where("thisAccount.id").is(accountId).and("thisAccount.bank.nationalIdentifier").is(bankId)
					.and("details.posted").lte(toDate).gte(fromDate);
		}
		else
		{
			cd = where("thisAccount.id").is(accountId).and("thisAccount.bank.nationalIdentifier").is(bankId);
		}

		Query query = null;

		if (pager != null)
		{
			query = query(cd).with(pager);
		}
		else
		{
			query = query(cd).with(sort);
		}
		return mongoTemplate.find(query, Transaction.class);
	}

}
