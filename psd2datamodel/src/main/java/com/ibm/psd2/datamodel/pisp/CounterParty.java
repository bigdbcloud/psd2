package com.ibm.psd2.datamodel.pisp;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "CounterParties")
@JsonInclude(value = Include.NON_EMPTY)
public class CounterParty
{
	@Id
	String id;
	
	TxnParty source;
	TxnParty party;
	String kind;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public TxnParty getSource()
	{
		return source;
	}
	public void setSource(TxnParty source)
	{
		this.source = source;
	}
	public TxnParty getParty()
	{
		return party;
	}
	public void setParty(TxnParty party)
	{
		this.party = party;
	}
	public String getKind()
	{
		return kind;
	}
	public void setKind(String kind)
	{
		this.kind = kind;
	}

	

}
