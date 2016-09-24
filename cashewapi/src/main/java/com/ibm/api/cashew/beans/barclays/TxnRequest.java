package com.ibm.api.cashew.beans.barclays;

import java.util.List;

public class TxnRequest
{
    private List<String> tags;

    private TxnAmount amount;

    private String description;

    private PaymentDescriptor paymentDescriptor;

    private String notes;

    private String paymentMethod;

    private List<String> metadata;

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public TxnAmount getAmount() {
		return amount;
	}

	public void setAmount(TxnAmount amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PaymentDescriptor getPaymentDescriptor() {
		return paymentDescriptor;
	}

	public void setPaymentDescriptor(PaymentDescriptor paymentDescriptor) {
		this.paymentDescriptor = paymentDescriptor;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public List<String> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<String> metadata) {
		this.metadata = metadata;
	}

   
}