package com.ibm.api.cashew.beans;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.Amount;

@Document(collection = "vochers")
@JsonInclude(value = Include.NON_EMPTY)
public class Voucher {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@Id
	private String code;
	private List<TxnDetails> acctFrom;
	private List<TxnDetails> redeemedTo;
	private boolean isRedeemed;
	private Amount amount;
	private String createdBy;
	private String creationDate;
	private String expiryDate;
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isRedeemed() {
		return isRedeemed;
	}

	public void setRedeemed(boolean isRedeemed) {
		this.isRedeemed = isRedeemed;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<TxnDetails> getRedeemedTo() {
		return redeemedTo;
	}

	public void setRedeemedTo(List<TxnDetails> redeemedTo) {
		this.redeemedTo = redeemedTo;
	}

	public List<TxnDetails> getAcctFrom() {
		return acctFrom;
	}

	public void setAcctFrom(List<TxnDetails> acctFrom) {
		this.acctFrom = acctFrom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

}
