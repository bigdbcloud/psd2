package com.ibm.api.cashew.reminder.services;

import java.util.List;

import com.ibm.api.cashew.beans.Reminder;

public interface ReminderService {
	
   public Reminder createReminder(Reminder reminder);
   public List<Reminder> getReminders(String userId, String fromDate, String toDate);
   public void deleteReminder(String id);
   public Reminder updateReminder(Reminder reminder);
	
}
