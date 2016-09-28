package com.ibm.psd2.integration;

import com.ibm.psd2.integration.jobs.StormStarter;

public class App
{
	public static void main(String[] args)
	{
		try
		{
			ArgumentsContainer ac = new ArgumentsContainer(args);
			StormStarter.startStorm(ac);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	}
}
