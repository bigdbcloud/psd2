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

import com.ibm.psd2.api.aip.dao.BankAccountDao;
import com.ibm.psd2.api.aip.dao.BankDao;
import com.ibm.psd2.api.aip.dao.TransactionDao;
import com.ibm.psd2.commons.beans.BankBean;
import com.ibm.psd2.commons.beans.SimpleResponseBean;
import com.ibm.psd2.commons.beans.aip.BankAccountDetailsBean;

@RestController
public class AdminController
{
	
	private static final Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	BankAccountDao badao;
	
	@Autowired
	BankDao bdao;

	@Autowired
	TransactionDao tdao;

	@Value("${version}")
	private String version;

	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/bank", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponseBean> createBank(@RequestBody BankBean b)
	{
		ResponseEntity<SimpleResponseBean> response;
		SimpleResponseBean srb = new SimpleResponseBean();
		try
		{
			if (b == null)
			{
				throw new IllegalArgumentException("No Bank Specified");
			}

			logger.info("BankBean = " + b.toString());

			bdao.createBank(b);
			
			srb.setResponseCode(SimpleResponseBean.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
		} catch (Exception e)
		{
			logger.error(e);
			srb.setResponseCode(SimpleResponseBean.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponseBean> createAccount(@RequestBody BankAccountDetailsBean b)
	{
		ResponseEntity<SimpleResponseBean> response;
		SimpleResponseBean srb = new SimpleResponseBean();
		try
		{
			if (b == null)
			{
				throw new IllegalArgumentException("No Account Specified");
			}

			logger.info("BankAccountDetailsBean = " + b.toString());

			badao.createBankAccountDetails(b);
			
			srb.setResponseCode(SimpleResponseBean.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
		} catch (Exception e)
		{
			logger.error(e);
			srb.setResponseCode(SimpleResponseBean.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

}
