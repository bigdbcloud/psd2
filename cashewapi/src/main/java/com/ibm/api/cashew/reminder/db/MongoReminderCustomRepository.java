package com.ibm.api.cashew.reminder.db;

import java.util.List;

import com.ibm.api.cashew.beans.Reminder;

public interface MongoReminderCustomRepository {
	
	public List<Reminder> getReminders(String userId, String fromDate, String toDate);
}
