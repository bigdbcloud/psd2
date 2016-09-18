package com.ibm.api.cashew.reminder.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.api.cashew.beans.Reminder;

public interface MongoReminderRepository extends MongoRepository<Reminder, String> ,MongoReminderCustomRepository{

}