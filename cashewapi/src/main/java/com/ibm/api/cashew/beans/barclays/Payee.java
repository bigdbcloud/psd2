package com.ibm.api.cashew.beans.barclays;

public class Payee {
	private String id;

	private String paymentDescriptorType;

	private String description;

	private String name;

	private String sortCode;

	private String accountNo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentDescriptorType() {
		return paymentDescriptorType;
	}

	public void setPaymentDescriptorType(String paymentDescriptorType) {
		this.paymentDescriptorType = paymentDescriptorType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortCode() {
		return sortCode;
	}

	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

}