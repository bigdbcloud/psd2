package com.ibm.psd2.integration.jobs;

import java.util.HashMap;

import com.ibm.psd2.integration.ArgumentsContainer;

public class JobSubmitterFactory
{

	private static JobSubmitterFactory jsf;

	private HashMap<String, JobSubmitter> submitters;

	private JobSubmitterFactory()
	{
		submitters = new HashMap<>();
	}

	public static synchronized JobSubmitterFactory getInstance()
	{
		if (jsf == null)
		{
			jsf = new JobSubmitterFactory();
		}
		return jsf;
	}

	public synchronized void createSubmitter(ArgumentsContainer ac)
	{
		String type = ac.getValue(ArgumentsContainer.KEYS.JOB_SUBMITTER_TYPE.key());
		JobSubmitter js = submitters.get(type);

		if (js == null)
		{
			if (type.equals("local"))
			{

			}
			else if (type.equals("storm"))
			{
				js = new StormJobSubmitter(ac);
				submitters.put("storm", js);
			}
		}
	}

	public JobSubmitter getJobSubmitter(ArgumentsContainer ac)
	{
		String type = ac.getValue(ArgumentsContainer.KEYS.JOB_SUBMITTER_TYPE.key());
		JobSubmitter js = submitters.get(type);

		if (js == null)
		{
			createSubmitter(ac);
			js = submitters.get(type);
		}

		return js;

	}
}
