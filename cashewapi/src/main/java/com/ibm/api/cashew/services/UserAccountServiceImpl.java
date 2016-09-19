package com.ibm.api.cashew.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.bank.db.MongoTransactionRepository;
import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.db.elastic.ElasticTransactionRepository;
import com.ibm.api.cashew.db.mongo.MongoUserAccountsRepository;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.utils.UUIDGenerator;

@Service
public class UserAccountServiceImpl implements UserAccountService {
	private Logger logger = LogManager.getLogger(UserAccountServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	MongoUserAccountsRepository muar;

	@Autowired
	Utils utils;

	@Value("${psd2.url}")
	private String psd2Url;

	@Value("${psd2.username}")
	private String psd2Username;

	private String psd2Authorization;

	@Value("${psd2.password}")
	private String psd2Password;

	@Autowired
	private MongoTransactionRepository mongoTxnRepo;

	@Autowired
	private ElasticTransactionRepository elasticTxnRepo;

	@Override
	public SubscriptionRequest subscribe(String username, SubscriptionRequest subscriptionRequest) {
		SubscriptionRequest res = null;
		logger.debug("subscribing to username = " + username);
		try {
			/*
			 * First save the account in local DB Then make a subscription
			 * request call
			 */
			String url = psd2Url + "/subscriptionRequest";

			URI uri = new URI(url);
			RequestEntity<SubscriptionRequest> re = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).body(subscriptionRequest);
			ResponseEntity<SubscriptionRequest> response = restTemplate.exchange(re, SubscriptionRequest.class);
			res = response.getBody();

			UserAccount ua = new UserAccount();
			ua.setId(UUIDGenerator.generateUUID());
			ua.setAppUsername(username);
			ua.setSubscriptionRequestId(res.getId());
			ua.setSubscriptionRequestStatus(res.getStatus());
			ua.setViewIds(res.getSubscriptionInfo().getViewIds());
			ua.setLimits(res.getSubscriptionInfo().getLimits());
			ua.setTransactionRequestTypes(res.getSubscriptionInfo().getTransactionRequestTypes());
			ua.setSubscriptionRequestChallengeId(res.getChallenge().getId());

			BankAccountDetailsView badv = new BankAccountDetailsView();
			badv.setBankId(res.getSubscriptionInfo().getBankId());
			badv.setUsername(res.getSubscriptionInfo().getUsername());
			badv.setId(res.getSubscriptionInfo().getAccountId());
			ua.setAccount(badv);
			muar.save(ua);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return res;
	}

	@Override
	public UserAccount answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca) {
		/**
		 * first answer challenge of PSD2API /subscription/{id} once
		 * subscription request completes. call the getAccountAPI to
		 * getAccountInformation
		 * /banks/{bankId}/accounts/{accountId}/{viewId}/account
		 */
		UserAccount ua = null;
		try {
			String url = psd2Url + "/subscription/" + sca.getSubscriptionRequestId();
			logger.debug("url = " + url);
			URI uri = new URI(url);
			RequestEntity<ChallengeAnswer> re = RequestEntity.patch(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).body(sca.getChallengeAnswer());
			ResponseEntity<SubscriptionInfo> response = restTemplate.exchange(re, SubscriptionInfo.class);
			SubscriptionInfo si = response.getBody();

			if (si != null) {
				url = psd2Url + "/banks/" + si.getBankId() + "/accounts/" + si.getAccountId() + "/"
						+ si.getViewIds().get(0).getId() + "/account";
				uri = new URI(url);
				logger.debug("url = " + url);

				RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
						.header("Authorization", getPSD2Authorization()).header("user", si.getUsername()).build();
				ResponseEntity<BankAccountDetailsView> res = restTemplate.exchange(rea, BankAccountDetailsView.class);

				BankAccountDetailsView bdv = res.getBody();
				if (bdv != null) {
					ua = muar.findByAppUsernameAndAccountIdAndAccountBankIdAndAccountUsername(sca.getAppUsername(),
							si.getAccountId(), si.getBankId(), si.getUsername());

					ua.setAccount(bdv);
					ua.getAccount().setBalance(null);
					ua.setSubscriptionInfoStatus(si.getStatus());
					muar.save(ua);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ua;
	}

	@Override
	public BankAccountDetailsView getAccountInformation(String appUser, String bankId, String accountId) {
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUser, accountId, bankId);
		BankAccountDetailsView bdv = null;
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE)) {
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		String url = psd2Url + "/banks/" + ua.getAccount().getBankId() + "/accounts/" + ua.getAccount().getId() + "/"
				+ ua.getViewIds().get(0).getId() + "/account";
		logger.debug("url = " + url);

		try {
			URI uri = new URI(url);

			RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).header("user", ua.getAccount().getUsername())
					.build();

			ResponseEntity<BankAccountDetailsView> res = restTemplate.exchange(rea, BankAccountDetailsView.class);

			bdv = res.getBody();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bdv;

	}

	@Override
	public List<Transaction> getTransactions(String appUser, String bankId, String accountId, String sortDirection,
			String fromDate, String toDate, String sortBy, Integer offset, Integer limit) {
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUser, accountId, bankId);
		List<Transaction> txns = null;
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE)) {
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		/**
		 * /banks/{bankId}/accounts/{accountId}/{viewId}/transactions
		 */
		String url = psd2Url + "/banks/" + ua.getAccount().getBankId() + "/accounts/" + ua.getAccount().getId() + "/"
				+ ua.getViewIds().get(0).getId() + "/transactions";
		logger.debug("url = " + url);

		try {
			URI uri = new URI(url);

			RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).header("user", ua.getAccount().getUsername())
					.header("obp_sort_direction", sortDirection).header("obp_from_date", fromDate)
					.header("obp_to_date", toDate).header("obp_sort_by", sortBy)
					.header("obp_offset", (offset == null) ? "0" : offset.toString())
					.header("obp_limit", (limit == null) ? "10" : offset.toString()).build();

			ResponseEntity<List> res = restTemplate.exchange(rea, List.class);

			txns = (List<Transaction>) res.getBody();

			// save data in mongo and elastic search

			if (!CollectionUtils.isEmpty(txns)) {

				mongoTxnRepo.save(txns);
				elasticTxnRepo.save(populateElasticTxnDetails(txns));
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return txns;

	}

	@Override
	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, String user) {

		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(user, payer.getAccountId(),
				payer.getBankId());
		TxnRequestDetails txnReqDetails = null;

		BankAccountDetailsView bdv = null;
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE)) {
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		try {

			String url = psd2Url
					+ "/banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests";

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Authorization", getPSD2Authorization());
			headers.add("Content-Type", "application/json");
			headers.add("user", ua.getAccount().getUsername());

			HttpEntity<TxnRequest> request = new HttpEntity<TxnRequest>(txnReq, headers);

			Map<String, String> uriVariables = new HashMap<String, String>();
			uriVariables.put("bankId", payer.getBankId());
			uriVariables.put("accountId", payer.getAccountId());
			uriVariables.put("viewId", ua.getViewIds().get(0).getId());
			uriVariables.put("txnType", txnType);

			txnReqDetails = restTemplate.postForObject(url, request, TxnRequestDetails.class, uriVariables);

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

		return txnReqDetails;
	}

	private String getPSD2Authorization() {
		if (psd2Authorization == null) {
			psd2Authorization = utils.createBase64AuthHeader(psd2Username, psd2Password);
		}
		return psd2Authorization;
	}

	private List<com.ibm.api.cashew.beans.Transaction> populateElasticTxnDetails(List<Transaction> txnList) {

		List<com.ibm.api.cashew.beans.Transaction> elasticTxnList = new ArrayList<com.ibm.api.cashew.beans.Transaction>();

		for (Transaction txn : txnList) {
			com.ibm.api.cashew.beans.Transaction elasticTxn = new com.ibm.api.cashew.beans.Transaction();
			elasticTxn.setId(txn.getId());

			TxnParty from = new TxnParty();
			from.setAccountId(txn.getThisAccount().getId());
			from.setBankId(txn.getThisAccount().getBank().getName());
			elasticTxn.setFrom(from);

			TxnParty to = new TxnParty();
			to.setAccountId(txn.getOtherAccount().getId());
			to.setBankId(txn.getOtherAccount().getBank().getName());

			elasticTxn.setTo(to);

			elasticTxn.setDetails(txn.getDetails());

			elasticTxnList.add(elasticTxn);
		}

		return elasticTxnList;

	}

	@Override
	public Transaction tagTransaction(String userId, String bankId, String accountId, String txnId, String tag) {

		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(userId, accountId, bankId);
		Transaction txn = null;

		BankAccountDetailsView bdv = null;
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE)) {
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		try {

			String url = psd2Url + "/banks/{bankId}/accounts/{accountId}/{viewId}/transaction/{txnId}/tag/{tag}";

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Authorization", utils.createBase64AuthHeader(psd2Username, psd2Password));
			headers.add("Content-Type", "application/json");

			HttpEntity request = new HttpEntity(headers);

			Map<String, String> uriVariables = new HashMap<String, String>();
			uriVariables.put("bankId", bankId);
			uriVariables.put("accountId", accountId);
			uriVariables.put("viewId", ua.getViewIds().get(0).getId());
			uriVariables.put("txnId", txnId);
			uriVariables.put("tag", tag);

			ResponseEntity<Transaction> response = restTemplate.exchange(url, HttpMethod.PATCH, request,
					Transaction.class, uriVariables);

			txn = response.getBody();

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

		return txn;
	}

}
