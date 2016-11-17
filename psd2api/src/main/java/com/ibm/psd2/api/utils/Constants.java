package com.ibm.psd2.api.utils;

import java.text.SimpleDateFormat;

public class Constants
{
	public static final String OWNER_VIEW = "owner";
	public static final int CHALLENGE_MAX_ATTEMPTS = 3;
	public static final String CURRENCY_EURO = "EUR";
	public static final String CURRENCY_GBP = "GBP";
	public static final String SORT_ASCENDING = "ASC";
	public static final String TXN_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String ERRMSG_NOT_SUBSCRIBED = "Not Subscribed";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	private Constants()
	{

	}

}
