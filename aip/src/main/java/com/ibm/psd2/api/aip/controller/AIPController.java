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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.aip.dao.BankAccountDao;
import com.ibm.psd2.api.aip.dao.BankDao;
import com.ibm.psd2.api.aip.dao.TransactionDao;
import com.ibm.psd2.api.aip.utils.BankAccountOverviewVisitor;
import com.ibm.psd2.api.aip.utils.BankAccountOwnerViewVisitor;
import com.ibm.psd2.api.commons.Constants;
import com.ibm.psd2.api.subscription.dao.SubscriptionDao;
import com.ibm.psd2.commons.beans.BankBean;
import com.ibm.psd2.commons.beans.aip.BankAccountDetailsBean;
import com.ibm.psd2.commons.beans.aip.BankAccountDetailsViewBean;
import com.ibm.psd2.commons.beans.aip.BankAccountOverviewBean;
import com.ibm.psd2.commons.beans.aip.TransactionBean;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfoBean;
import com.ibm.psd2.commons.beans.subscription.ViewIdBean;
import com.ibm.psd2.commons.controller.APIController;

@RestController
public class AIPController extends APIController {
	private static final Logger logger = LogManager.getLogger(AIPController.class);

	@Autowired
	BankAccountDao bad;

	@Autowired
	BankDao bdao;

	@Autowired
	SubscriptionDao sdao;

	@Autowired
	TransactionDao tdao;

	@Value("${version}")
	private String version;

	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<BankAccountOverviewBean>> getBankAccounts(
			@PathVariable("bankId") String bankId, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client) {

		ResponseEntity<List<BankAccountOverviewBean>> response;
		try {
			ViewIdBean ownerView = new ViewIdBean();
			ownerView.setId(Constants.OWNER_VIEW);

			List<SubscriptionInfoBean> lstSib = sdao.getSubscriptionInfo(user, client,
					bankId);

			if (lstSib == null) {
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			List<String> accountIds = new ArrayList<>();
			for (Iterator<SubscriptionInfoBean> iterator = lstSib.iterator(); iterator.hasNext();) {
				SubscriptionInfoBean s = iterator.next();
				if (validateSubscription(s, ownerView)) {
					accountIds.add(s.getAccountId());
				}
			}

			List<BankAccountDetailsBean> ba = bad.getBankAccounts(user, bankId);

			List<BankAccountOverviewBean> accountList = null;
			BankAccountOverviewVisitor baoVisitor = new BankAccountOverviewVisitor();
			for (Iterator<BankAccountDetailsBean> iterator = ba.iterator(); iterator.hasNext();) {

				BankAccountDetailsBean b = iterator.next();

				if (!accountIds.isEmpty() && accountIds.contains(b.getId())) {
					b.registerVisitor(BankAccountOverviewBean.class.getName() + ":" + Constants.OWNER_VIEW, baoVisitor);

					if (accountList == null) {
						accountList = new ArrayList<>();
					}
					accountList.add(b.getBankAccountOverview(Constants.OWNER_VIEW));
				}
			}

			response = ResponseEntity.ok(accountList);
		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankAccountDetailsViewBean> getAccountById(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client) {
		ResponseEntity<BankAccountDetailsViewBean> response;
		try {
			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView)) {
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			BankAccountDetailsBean b = bad.getBankAccountDetails(bankId, accountId, user);
			BankAccountDetailsViewBean bo = null;

			if (b == null) {
				throw new IllegalArgumentException("Account Not Found");
			}

			BankAccountOwnerViewVisitor bv = new BankAccountOwnerViewVisitor();
			if (Constants.OWNER_VIEW.equals(viewId)) {
				b.registerVisitor(BankAccountDetailsViewBean.class.getName() + ":" + viewId, bv);
				bo = b.getBankAccountDetails(viewId);
				response = ResponseEntity.ok(bo);
			} else {
				throw new IllegalArgumentException(
						"View ID is incorrect. Currently supported ones are: " + Constants.OWNER_VIEW);
			}
		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/my/banks/{bankId}/accounts/{accountId}/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankAccountDetailsViewBean> getAccountById(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client) {
		ResponseEntity<BankAccountDetailsViewBean> response;
		try {
			ViewIdBean ownerView = new ViewIdBean();
			ownerView.setId(Constants.OWNER_VIEW);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);

			if (!validateSubscription(sib, ownerView)) {
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			BankAccountDetailsBean b = bad.getBankAccountDetails(bankId, accountId, user);

			if (b == null) {
				throw new IllegalArgumentException("Account Not Found");
			}

			BankAccountOwnerViewVisitor bv = new BankAccountOwnerViewVisitor();
			b.registerVisitor(BankAccountDetailsViewBean.class.getName() + ":" + Constants.OWNER_VIEW, bv);
			BankAccountDetailsViewBean bo = b.getBankAccountDetails(Constants.OWNER_VIEW);
			response = ResponseEntity.ok(bo);

		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions/{txnId}/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TransactionBean> getTransactionById(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId,
			@PathVariable("txnId") String txnId, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client) {
		ResponseEntity<TransactionBean> response;
		try {
			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView)) {
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			TransactionBean t = tdao.getTransactionById(bankId, accountId, txnId);

			response = ResponseEntity.ok(t);

		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TransactionBean>> getTransactions(@PathVariable("bankId") String bankId,
			@PathVariable("accountId") String accountId, @PathVariable("viewId") String viewId,
			@RequestHeader(value = "obp_sort_direction", required = false) String sortDirection,
			@RequestHeader(value = "obp_limit", required = false) Integer limit,
			@RequestHeader(value = "obp_from_date", required = false) String fromDate,
			@RequestHeader(value = "obp_to_date", required = false) String toDate,
			@RequestHeader(value = "obp_sort_by", required = false) String sortBy,
			@RequestHeader(value = "obp_offset", required = false) Integer number, @RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client) {
		ResponseEntity<List<TransactionBean>> response;
		try {
			ViewIdBean specifiedView = new ViewIdBean();
			specifiedView.setId(viewId);

			SubscriptionInfoBean sib = sdao.getSubscriptionInfo(user, client,
					accountId, bankId);
			if (!validateSubscription(sib, specifiedView)) {
				throw new IllegalAccessException(Constants.ERRMSG_NOT_SUBSCRIBED);
			}

			List<TransactionBean> t = tdao.getTransactions(bankId, accountId, sortDirection, limit, fromDate, toDate,
					sortBy, number);

			response = ResponseEntity.ok(t);

		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<BankBean>> getBanks() {
		ResponseEntity<List<BankBean>> response;
		try {
			List<BankBean> b = bdao.getBanks();
			response = ResponseEntity.ok(b);
		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/banks/{bankId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<BankBean> getBankById(@PathVariable("bankId") String bankId) {
		ResponseEntity<BankBean> response;
		try {
			BankBean b = bdao.getBankDetails(bankId);
			response = ResponseEntity.ok(b);
		} catch (Exception ex) {
			logger.error(ex);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

}
