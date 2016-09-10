package com.ibm.psd2.api.admin.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.aip.services.BankAccountDetailsService;
import com.ibm.psd2.api.aip.services.BankService;
import com.ibm.psd2.api.aip.services.TransactionStatementService;
import com.ibm.psd2.commons.datamodel.Bank;
import com.ibm.psd2.commons.datamodel.SimpleResponse;
import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;

@RestController
public class AdminController
{
	
	private static final Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	BankAccountDetailsService badao;
	
	@Autowired
	BankService bdao;

	@Autowired
	TransactionStatementService tdao;

	@Value("${version}")
	private String version;

	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/bank", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponse> createBank(@RequestBody Bank b)
	{
		ResponseEntity<SimpleResponse> response;
		SimpleResponse srb = new SimpleResponse();
		try
		{
			if (b == null)
			{
				throw new IllegalArgumentException("No Bank Specified");
			}

			logger.info("Bank = " + b.toString());

			bdao.createBank(b);
			
			srb.setResponseCode(SimpleResponse.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponse> createAccount(@RequestBody BankAccountDetails b)
	{
		ResponseEntity<SimpleResponse> response;
		SimpleResponse srb = new SimpleResponse();
		try
		{
			if (b == null)
			{
				throw new IllegalArgumentException("No Account Specified");
			}

			logger.info("BankAccountDetails = " + b.toString());

			badao.createBankAccountDetails(b);
			
			srb.setResponseCode(SimpleResponse.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

}
