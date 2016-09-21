package com.ibm.api.cashew.db.mongo;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;

import com.ibm.api.cashew.beans.Reminder;

public class MongoReminderRepositoryImpl implements MongoReminderCustomRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Reminder> getReminders(String userId, String fromDate, String toDate) {

		Sort sort = new Sort(Sort.Direction.ASC, "reminderDate");

		CriteriaDefinition cd = null;

		if (fromDate == null || toDate == null) {

			fromDate = Reminder.DATE_FORMAT.format(new Date());
			cd = where("createdBy").is(userId).and("reminderDate").gte(fromDate);
		} else {

			cd = where("createdBy").is(userId).and("reminderDate").lte(toDate).gte(fromDate);
		}

		Query query = query(cd).with(sort);
		;
		return mongoTemplate.find(query, Reminder.class);
	}

}
