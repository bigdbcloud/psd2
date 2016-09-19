package com.ibm.api.cashew.beans.aggregation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_EMPTY)
public abstract class AggregationRequest
{
	private AggregationTypes type;
	private String name;
	private FieldBean field;
	private ScriptBean script;
	
	public AggregationTypes getType()
	{
		return type;
	}

	public void setType(AggregationTypes type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ScriptBean getScript()
	{
		return script;
	}

	public void setScript(ScriptBean script)
	{
		this.script = script;
	}

	public FieldBean getField()
	{
		return field;
	}

	public void setField(FieldBean field)
	{
		this.field = field;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AggregationRequest other = (AggregationRequest) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
