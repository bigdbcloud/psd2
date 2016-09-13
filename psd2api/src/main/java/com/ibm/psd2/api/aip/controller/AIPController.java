package com.ibm.psd2.api.aip.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.APIController;
import com.ibm.psd2.api.aip.services.BankAccountDetailsService;
import com.ibm.psd2.api.aip.services.BankAccountOverviewVisitor;
import com.ibm.psd2.api.aip.services.BankAccountOwnerViewVisitor;
import com.ibm.psd2.api.aip.services.BankService;
import com.ibm.psd2.api.aip.services.TransactionStatementService;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.commons.datamodel.Bank;
import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.commons.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.commons.datamodel.aip.BankAccountOverview;
import com.ibm.psd2.commons.datamodel.aip.Transaction;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.datamodel.subscription.ViewId;
import com.ibm.psd2.commons.subscription.service.SubscriptionService;

@RestController
public class AIPController extends APIController
{
	private final Logger logger = LogManager.getLogger(AIPController.class);

	@Autowired
	BankAccountDetailsService bad;

	@Autowired
	BankService bdao;

	@Autowired
	SubscriptionService sdao;

	@Autowired
	TransactionStatementService tdao;

	@Value("${version}")
	private String version;

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId, 'owner')")
	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<BankAccountOverview>> getBankAccounts(
			@PathVariable("bankId") String bankId)
	{
		ResponseEntity<List<BankAccountOverview>> response;
		try
		{
			OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
			String user = (String) auth.getPrincipal();
			ViewId ownerView = new ViewId();
			ownerView.setId(Constants.OWNER_VIEW);

			List<SubscriptionInfo> lstSib = sdao.getSubscriptionInfo(user, auth.getOAuth2Request().getClientId(),
					bankId);

			List<String> accountIds = new ArrayList<>();
			for (Iterator<SubscriptionInfo> iterator = lstSib.iterator(); iterator.hasNext();)
			{
				SubscriptionInfo s = iterator.next();
				if (validateSubscription(s, ownerView))
				{
					accountIds.add(s.getAccountId());
				}
			}

			List<BankAccountDetails> ba = bad.getBankAccounts(user, bankId);

			List<BankAccountOverview> accountList = null;
			BankAccountOverviewVisitor baoVisitor = new BankAccountOverviewVisitor();
			for (Iterator<BankAccountDetails> iterator = ba.iterator(); iterator.hasNext();)
			{
				BankAccountDetails b = iterator.next();
				if (!accountIds.isEmpty() && accountIds.contains(b.getId()))
				{
					b.registerVisitor(BankAccountOverview.class.getName() + ":" + Constants.OWNER_VIEW, baoVisitor);
					if (accountList == null)
					{
						accountList = new ArrayList<>();
					}
					accountList.add(b.getBankAccountOverview(Constants.OWNER_VIEW));
				}
			}
			response = ResponseEntity.ok(accountList);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankAccountDetailsView> getAccountById(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId)
	{
		ResponseEntity<BankAccountDetailsView> response;
		try
		{

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			BankAccountDetails b = bad.getBankAccountDetails(bankId, accountId, (String) auth.getPrincipal());
			BankAccountDetailsView bo = null;

			if (b == null)
			{
				throw new IllegalArgumentException("Account Not Found");
			}

			BankAccountOwnerViewVisitor bv = new BankAccountOwnerViewVisitor();
			if (Constants.OWNER_VIEW.equals(viewId))
			{
				b.registerVisitor(BankAccountDetailsView.class.getName() + ":" + viewId, bv);
				bo = b.getBankAccountDetails(viewId);
				response = ResponseEntity.ok(bo);
			}
			else
			{
				throw new IllegalArgumentException(
						"View ID is incorrect. Currently supported ones are: " + Constants.OWNER_VIEW);
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, 'owner')")
	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts/{accountId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankAccountDetailsView> getAccountById(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId)
	{
		ResponseEntity<BankAccountDetailsView> response;
		try
		{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String user = (String) auth.getPrincipal();
			BankAccountDetails b = bad.getBankAccountDetails(bankId, accountId, user);

			if (b == null)
			{
				throw new IllegalArgumentException("Account Not Found");
			}

			BankAccountOwnerViewVisitor bv = new BankAccountOwnerViewVisitor();
			b.registerVisitor(BankAccountDetailsView.class.getName() + ":" + Constants.OWNER_VIEW, bv);
			BankAccountDetailsView bo = b.getBankAccountDetails(Constants.OWNER_VIEW);
			response = ResponseEntity.ok(bo);

		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions/{txnId}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Transaction> getTransactionById(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId,
			@PathVariable("txnId") String txnId)
	{
		ResponseEntity<Transaction> response;
		try
		{
			// Authentication auth =
			// SecurityContextHolder.getContext().getAuthentication();
			// String user = (String) auth.getPrincipal();
			Transaction t = tdao.getTransactionById(bankId, accountId, txnId);
			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write') && hasPermission(#bankId + '.' + #accountId, #viewId)")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<Transaction>> getTransactions(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId,
			@RequestHeader(value = "obp_sort_direction", required = false) String sortDirection,
			@RequestHeader(value = "obp_limit", required = false) Integer limit,
			@RequestHeader(value = "obp_from_date", required = false) String fromDate,
			@RequestHeader(value = "obp_to_date", required = false) String toDate,
			@RequestHeader(value = "obp_sort_by", required = false) String sortBy,
			@RequestHeader(value = "obp_offset", required = false) Integer offset)
	{
		ResponseEntity<List<Transaction>> response;
		try
		{
			if (offset == null)
			{
				offset = 0;
			}

			if (limit == null || limit == 0)
			{
				limit = 10;
			}

			List<Transaction> t = tdao.getTransactions(bankId, accountId, sortDirection, fromDate, toDate, sortBy,
					offset / limit, limit);

			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<Bank>> getBanks()
	{
		ResponseEntity<List<Bank>> response;
		try
		{
			List<Bank> b = bdao.getBanks();
			response = ResponseEntity.ok(b);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Bank> getBankById(@PathVariable("bankId") String bankId)
	{
		ResponseEntity<Bank> response;
		try
		{
			Bank b = bdao.getBankDetails(bankId);
			response = ResponseEntity.ok(b);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
