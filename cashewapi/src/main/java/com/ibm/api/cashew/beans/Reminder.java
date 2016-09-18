package com.ibm.api.cashew.beans;

import java.text.SimpleDateFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.Amount;
import com.ibm.psd2.datamodel.pisp.TxnParty;

@Document(collection = "reminders")
@JsonInclude(value = Include.NON_EMPTY)
public class Reminder {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public static enum Type
	{
		GENERAL("general"), PAYMENT("payment");

		private String value;

		Type(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return this.value;
		}
	}

	@Id
	private String id;
	private String description;
	private String creationDate;
	private String createdBy;
	private String reminderDate;
	private int frequency;
	private String reminderType;
	private TxnParty from;
	private TxnParty to;
	private Amount amount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(String reminderDate) {
		this.reminderDate = reminderDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReminderType() {
		return reminderType;
	}

	public void setReminderType(String reminderType) {
		this.reminderType = reminderType;
	}

	public TxnParty getFrom() {
		return from;
	}

	public void setFrom(TxnParty from) {
		this.from = from;
	}

	public TxnParty getTo() {
		return to;
	}

	public void setTo(TxnParty to) {
		this.to = to;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}
