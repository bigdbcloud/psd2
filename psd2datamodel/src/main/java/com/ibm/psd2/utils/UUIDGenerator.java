package com.ibm.psd2.utils;

import java.util.Random;
import java.util.UUID;

public class UUIDGenerator
{
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final Random random = new Random();

	public static String generateUUID()
	{
		return UUID.randomUUID().toString();
	}

	public static String randomAlphaNumeric(int length)
	{
		StringBuilder builder = new StringBuilder();
		while (length-- != 0)
		{
			int character = random.nextInt(ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
}
