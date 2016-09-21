package com.ibm.api.cashew.services.ibmbank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IBMPaymentsServiceImpl implements IBMPaymentsService
{
	private Logger logger = LogManager.getLogger(IBMPaymentsServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	IBMPSD2Credentials psd2Credentials;



}
