package com.ibm.api.cashew.services.ibmbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.api.cashew.utils.Utils;

@Component
public class IBMPSD2Credentials
{
	@Autowired
	Utils utils;

	@Value("${psd2.url}")
	private String psd2Url;

	@Value("${psd2.username}")
	private String psd2Username;

	private String psd2Authorization;

	@Value("${psd2.password}")
	private String psd2Password;

	protected String getPSD2Authorization()
	{
		if (psd2Authorization == null)
		{
			psd2Authorization = utils.createBase64AuthHeader(psd2Username, psd2Password);
		}
		return psd2Authorization;
	}

	public String getPsd2Url()
	{
		return psd2Url;
	}
}
