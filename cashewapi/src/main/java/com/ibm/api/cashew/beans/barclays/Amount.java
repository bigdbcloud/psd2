package com.ibm.api.cashew.beans.barclays;

public class Amount {
	private String moneyOut;

	private String moneyIn;

	public String getMoneyOut() {
		return moneyOut;
	}

	public void setMoneyOut(String moneyOut) {
		this.moneyOut = moneyOut;
	}

	public String getMoneyIn() {
		return moneyIn;
	}

	public void setMoneyIn(String moneyIn) {
		this.moneyIn = moneyIn;
	}

	@Override
	public String toString() {
		return "ClassPojo [moneyOut = " + moneyOut + ", moneyIn = " + moneyIn + "]";
	}
}
