package com.ibm.psd2.oauth2server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils
{
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static double getAgeInYears(String dob)
	{
		if (dob == null)
		{
			return 0;
		}

		Date ddob = null;
		Date currentDate = new Date();
		try
		{
			ddob = DATE_FORMAT.parse(dob);
		}
		catch (ParseException e)
		{
			throw new IllegalArgumentException(e);
		}
		
		if (ddob.getTime() > currentDate.getTime())
		{
			throw new IllegalArgumentException("Date of Birth can't be greater than today's date");
		}
		
		return (currentDate.getTime() - ddob.getTime())/(1000*60*60*24*12);
	}
}
