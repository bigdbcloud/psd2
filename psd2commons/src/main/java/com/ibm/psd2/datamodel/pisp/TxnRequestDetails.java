package com.ibm.psd2.datamodel.pisp;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.psd2.datamodel.Challenge;

@Document(collection = "TxnRequests")
@JsonInclude(value = Include.NON_EMPTY)
public class TxnRequestDetails implements Serializable
{
	public static final String TXN_STATUS_INITIATED="INITIATED";
	public static final String TXN_STATUS_COMPLETED="COMPLETED";
	public static final String TXN_STATUS_PENDING="PENDING";
	public static final String TXN_STATUS_FAILED="FAILED";
	
	@Id
	private String id;
	private String type;
	private TxnParty from;
	private TxnRequest body;
	String transactionIds;
	String status;
	Date startDate;
	Date endDate;
	private Challenge challenge;
	private TxnCharge charge;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public TxnParty getFrom()
	{
		return from;
	}
	public void setFrom(TxnParty from)
	{
		this.from = from;
	}
	public TxnRequest getBody()
	{
		return body;
	}
	public void setBody(TxnRequest body)
	{
		this.body = body;
	}
	public String getTransactionIds()
	{
		return transactionIds;
	}
	public void setTransactionIds(String transaction_ids)
	{
		this.transactionIds = transaction_ids;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date start_date)
	{
		this.startDate = start_date;
	}
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date end_date)
	{
		this.endDate = end_date;
	}
	public Challenge getChallenge()
	{
		return challenge;
	}
	public void setChallenge(Challenge challenge)
	{
		this.challenge = challenge;
	}
	public TxnCharge getCharge()
	{
		return charge;
	}
	public void setCharge(TxnCharge charge)
	{
		this.charge = charge;
	}

	public String toString()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(this);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
