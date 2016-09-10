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
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.aip.services.BankAccountDetailsService;
import com.ibm.psd2.api.aip.services.BankAccountOverviewVisitor;
import com.ibm.psd2.api.aip.services.BankAccountOwnerViewVisitor;
import com.ibm.psd2.api.aip.services.BankService;
import com.ibm.psd2.api.aip.services.TransactionStatementService;
import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.commons.controller.APIController;
import com.ibm.psd2.commons.datamodel.Bank;
import com.ibm.psd2.commons.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.commons.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.commons.datamodel.aip.BankAccountOverview;
import com.ibm.psd2.commons.datamodel.aip.TransactionBean;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.datamodel.subscription.ViewId;

@RestController
public class AIPController extends APIController
{
	private static final Logger logger = LogManager.getLogger(AIPController.class);

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

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<BankAccountOverview>> getBankAccounts(
			@PathVariable("bankId") String bankId, Authentication auth)
	{

		ResponseEntity<List<BankAccountOverview>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId ownerView = new ViewId();
			ownerView.setId(Constants.OWNER_VIEW);

			List<SubscriptionInfo> lstSib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					bankId);

			if (lstSib == null)
			{
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

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
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankAccountDetailsView> getAccountById(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, Authentication auth)
	{
		ResponseEntity<BankAccountDetailsView> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			BankAccountDetails b = bad.getBankAccountDetails(bankId, accountId, user);
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
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts/{accountId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankAccountDetailsView> getAccountById(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId, Authentication auth)
	{
		ResponseEntity<BankAccountDetailsView> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId ownerView = new ViewId();
			ownerView.setId(Constants.OWNER_VIEW);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);

			if (!validateSubscription(sib, ownerView))
			{
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

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
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions/{txnId}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TransactionBean> getTransactionById(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId,
			@PathVariable("txnId") String txnId, Authentication auth)
	{
		ResponseEntity<TransactionBean> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			TransactionBean t = tdao.getTransactionById(bankId, accountId, txnId);

			response = ResponseEntity.ok(t);

		}
		catch (Exception ex)
		{
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@PreAuthorize("#oauth2.hasScope('write')")
	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionBean>> getTransactions(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId,
			@RequestHeader(value = "obp_sort_direction", required = false) String sortDirection,
			@RequestHeader(value = "obp_limit", required = false) Integer limit,
			@RequestHeader(value = "obp_from_date", required = false) String fromDate,
			@RequestHeader(value = "obp_to_date", required = false) String toDate,
			@RequestHeader(value = "obp_sort_by", required = false) String sortBy,
			@RequestHeader(value = "obp_offset", required = false) Integer offset, Authentication auth)
	{
		ResponseEntity<List<TransactionBean>> response;
		try
		{
			OAuth2Authentication oauth2 = (OAuth2Authentication) auth;
			String user = (String) auth.getPrincipal();
			ViewId specifiedView = new ViewId();
			specifiedView.setId(viewId);

			SubscriptionInfo sib = sdao.getSubscriptionInfo(user, oauth2.getOAuth2Request().getClientId(),
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView))
			{
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			if (offset == null)
			{
				offset = 0;
			}

			if (limit == null || limit == 0)
			{
				limit = 10;
			}
			
			List<TransactionBean> t = tdao.getTransactions(bankId, accountId, sortDirection, fromDate, toDate, sortBy,
					offset / limit, limit);

			response = ResponseEntity.ok(t);
		}
		catch (Exception ex)
		{
			logger.error(ex);
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
			logger.error(ex);
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
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

}
