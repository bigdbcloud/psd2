package com.ibm.psd2.integration.jobs;

import com.ibm.psd2.integration.ArgumentsContainer;

public class StormStarter
{
	public static void startStorm(ArgumentsContainer ac) throws Exception
	{
		System.out.println("Brewing Storm PSD2....");
		JobSubmitterFactory jsf = JobSubmitterFactory.getInstance();

		JobSubmitter js = jsf.getJobSubmitter(ac);
		js.submitJob();
	}
}
