package com.ibm.api.cashew.utils;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Utils
{
	public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public double getAgeInYears(String dob)
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

		return (currentDate.getTime() - ddob.getTime()) / (1000 * 60 * 60 * 24 * 12);
	}

	public String createBase64AuthHeader(String username, String password)
	{
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		return authHeader;
	}

}
