package com.ibm.api.cashew.beans.barclays;

public class Card {
	private String customerId;

	private String expiryDate;

	private String currentBalance;

	private String maxSpend;

	private String type;

	private String displayName;

	private String cardNumber;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getMaxSpend() {
		return maxSpend;
	}

	public void setMaxSpend(String maxSpend) {
		this.maxSpend = maxSpend;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return "Card [customerId=" + customerId + ", expiryDate=" + expiryDate + ", currentBalance=" + currentBalance
				+ ", maxSpend=" + maxSpend + ", type=" + type + ", displayName=" + displayName + ", cardNumber="
				+ cardNumber + "]";
	}
}
