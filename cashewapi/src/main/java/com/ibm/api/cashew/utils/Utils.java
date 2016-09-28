package com.ibm.api.cashew.utils;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.api.cashew.beans.Voucher;

@Component
public class Utils
{
	public final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private Set<String> tagSet;

	@Value("${default.tags}")
	private String tags;

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

	public String getVocherCode()
	{

		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder("Cashew" + "-" + (100000 + rnd.nextInt(900000)) + "-");
		for (int i = 0; i < 5; i++)
			sb.append(chars[rnd.nextInt(chars.length)]);

		return sb.toString();
	}

	public String getVocherExpDate()
	{

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, 12);
		return Voucher.DATE_FORMAT.format(c.getTime());

	}

	public Set<String> getTags()
	{

		if (tagSet == null)
		{
			StringTokenizer st = new StringTokenizer(tags, ",");
			tagSet = new HashSet<>();
			while (st.hasMoreTokens())
			{
				String tag = st.nextToken();
				tagSet.add(tag);
			}
		}
		return tagSet;

	}

}
