package com.ibm.api.cashew.beans;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ibm.psd2.datamodel.aip.TransactionDetails;
import com.ibm.psd2.datamodel.pisp.TxnParty;

@Document(indexName = "transactions", type = "transaction")
@JsonInclude(value = Include.NON_EMPTY)
public class Transaction {
	
	@Id
	private String id;
	private TxnParty from;
	private TxnParty to;
	private TransactionDetails details;
	private User userInfo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public TransactionDetails getDetails() {
		return details;
	}
	public void setDetails(TransactionDetails details) {
		this.details = details;
	}
	public User getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(User userInfo) {
		this.userInfo = userInfo;
	}
}
