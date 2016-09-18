package com.ibm.api.cashew.reminder.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.api.cashew.beans.Reminder;
import com.ibm.api.cashew.reminder.db.MongoReminderRepository;
import com.ibm.api.cashew.utils.UUIDGenerator;

@Service
public class ReminderServiceImpl implements ReminderService {

	@Autowired
	private MongoReminderRepository mongoReminderRepo;

	@Override
	public Reminder createReminder(Reminder reminder) {

		validateReminder(reminder);

		reminder.setId(UUIDGenerator.generateUUID());
		reminder.setCreationDate(Reminder.DATE_FORMAT.format(new Date()));
		return mongoReminderRepo.save(reminder);

	}

	@Override
	public List<Reminder> getReminders(String userId, String fromDate, String toDate) {

		return mongoReminderRepo.getReminders(userId,fromDate,toDate);
	}

	@Override
	public void deleteReminder(String id) {

		Reminder reminder = mongoReminderRepo.findOne(id);

		if (reminder == null) {

			throw new IllegalArgumentException("Invalid reminder Id");

		}
		mongoReminderRepo.delete(id);

	}

	

	@Override
	public Reminder updateReminder(Reminder reminder) {

		validateReminder(reminder);

		Reminder reminderExist = mongoReminderRepo.findOne(reminder.getId());

		if (reminderExist == null) {

			throw new IllegalArgumentException("Invalid reminder Id");

		}

		return mongoReminderRepo.save(reminder);

	}
	
	private void validateReminder(Reminder reminder) {

		if (reminder == null) {

			throw new IllegalArgumentException("Reminder details are required");
		}

		if (StringUtils.isBlank(reminder.getDescription())) {

			throw new IllegalArgumentException("Reminder description cannot be blank");
		}

		if (StringUtils.isBlank(reminder.getReminderType())) {

			throw new IllegalArgumentException("Reminder type cannot be blank");
		}

		if (getReminderTypes().contains(reminder.getReminderType())) {

			throw new IllegalArgumentException("Invalid reminder type");
		}

		if (StringUtils.isBlank(reminder.getReminderDate())) {

			throw new IllegalArgumentException("Reminder date is required");
		}

	}

	private Set<String> getReminderTypes() {

		Set<String> remTypes = new HashSet<String>();
		for (Reminder.Type remType : Reminder.Type.values()) {

			remTypes.add(remType.name());
		}
		return remTypes;

	}

}
