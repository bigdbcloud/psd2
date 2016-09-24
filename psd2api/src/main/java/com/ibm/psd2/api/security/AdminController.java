package com.ibm.psd2.api.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.aip.services.BankAccountDetailsService;
import com.ibm.psd2.api.aip.services.BankService;
import com.ibm.psd2.api.pisp.service.CounterPartyService;
import com.ibm.psd2.api.pisp.service.TransactionRequestService;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.datamodel.Bank;
import com.ibm.psd2.datamodel.SimpleResponse;
import com.ibm.psd2.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.datamodel.pisp.CounterParty;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;

@RestController
public class AdminController
{

	private static final Logger logger = LogManager.getLogger(AdminController.class);

	@Autowired
	TransactionRequestService txnReqService;

	@Autowired
	SubscriptionService subscriptionService;
	
	@Autowired
	BankAccountDetailsService badao;
	
	@Autowired
	CounterPartyService cps;

	@Autowired
	BankService bdao;

	@Value("${version}")
	private String version;
	
	@Autowired
	MongoAPIClientRepository macr;
	
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
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/counterparty", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponse> createPayee(@RequestBody CounterParty cp)
	{
		ResponseEntity<SimpleResponse> response;
		SimpleResponse srb = new SimpleResponse();
		try
		{
			if (cp == null)
			{
				throw new IllegalArgumentException("No Bank Specified");
			}

			logger.info("Counter Party = " + cp);

			cps.createCounterParty(cp);

			srb.setResponseCode(SimpleResponse.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
		}
		catch (Exception e)
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
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/admin/clientsignup", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponse> createClient(@RequestBody(required = true) APIClient client)
	{
		ResponseEntity<SimpleResponse> response;
		SimpleResponse srb = new SimpleResponse();
		try
		{
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			client.setLocked(false);
			client.setPassword(encoder.encode(client.getPassword()));

			if (macr.findOne(client.getClientname()) != null)
			{
				throw new IllegalArgumentException ("Client already exists");
			}
			
			macr.save(client);
			srb.setResponseMessage("SUCCESS");
			srb.setResponseCode(SimpleResponse.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/admin/banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TxnRequestDetails> createTransactionRequest(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @PathVariable("txnType") String txnType,
			@RequestBody(required = true) TxnRequest trb,
			@RequestHeader(value = "fromDate", required = true) String fromDate,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<TxnRequestDetails> response;
		try
		{
			if (trb == null || trb.getTo() == null
					|| (accountId.equals(trb.getTo().getAccountId()) && bankId.equals(trb.getTo().getBankId())))
			{
				throw new IllegalArgumentException("Invalid Transaction Request");
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			SubscriptionInfo sib = subscriptionService.getSubscriptionInfo(user, (String) auth.getName(), accountId, bankId);
			TxnParty payee = new TxnParty(bankId, accountId);
			TxnRequestDetails t = txnReqService.createTransactionRequestHack(sib, trb, payee, txnType, fromDate);

			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}	
}
