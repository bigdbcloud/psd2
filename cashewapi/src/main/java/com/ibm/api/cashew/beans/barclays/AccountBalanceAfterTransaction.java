package com.ibm.api.cashew.beans.barclays;

public class AccountBalanceAfterTransaction {
	private String amount;

	private String position;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "ClassPojo [amount = " + amount + ", position = " + position + "]";
	}
}