package com.ibm.api.cashew.services;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.db.MongoUserAccountsRepository;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.utils.UUIDGenerator;

@Service
public class UserAccountServiceImpl implements UserAccountService
{
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

	@Override
	public SubscriptionRequest subscribe(String username, SubscriptionRequest subscriptionRequest)
	{
		SubscriptionRequest res = null;
		logger.debug("subscribing to username = " + username);
		try
		{
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
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return res;
	}

	@Override
	public UserAccount answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca)
	{
		/**
		 * first answer challenge of PSD2API /subscription/{id} once
		 * subscription request completes. call the getAccountAPI to
		 * getAccountInformation
		 * /banks/{bankId}/accounts/{accountId}/{viewId}/account
		 */
		UserAccount ua = null;
		try
		{
			String url = psd2Url + "/subscription/" + sca.getSubscriptionRequestId();
			logger.debug("url = " + url);
			URI uri = new URI(url);
			RequestEntity<ChallengeAnswer> re = RequestEntity.patch(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).body(sca.getChallengeAnswer());
			ResponseEntity<SubscriptionInfo> response = restTemplate.exchange(re, SubscriptionInfo.class);
			SubscriptionInfo si = response.getBody();

			if (si != null)
			{
				url = psd2Url + "/banks/" + si.getBankId() + "/accounts/" + si.getAccountId() + "/"
						+ si.getViewIds().get(0).getId() + "/account";
				uri = new URI(url);
				logger.debug("url = " + url);

				RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
						.header("Authorization", getPSD2Authorization()).header("user", si.getUsername()).build();
				ResponseEntity<BankAccountDetailsView> res = restTemplate.exchange(rea, BankAccountDetailsView.class);

				BankAccountDetailsView bdv = res.getBody();
				if (bdv != null)
				{
					ua = muar.findByAppUsernameAndAccountIdAndAccountBankIdAndAccountUsername(sca.getAppUsername(),
							si.getAccountId(), si.getBankId(), si.getUsername());

					ua.setAccount(bdv);
					ua.getAccount().setBalance(null);
					ua.setSubscriptionInfoStatus(si.getStatus());
					muar.save(ua);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return ua;
	}

	@Override
	public BankAccountDetailsView getAccountInformation(String appUser, String bankId, String accountId, String viewId)
	{
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUser, accountId, bankId);
		BankAccountDetailsView bdv = null;
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE))
		{
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		String url = psd2Url + "/banks/" + ua.getAccount().getBankId() + "/accounts/" + ua.getAccount().getId() + "/"
				+ viewId + "/account";
		logger.debug("url = " + url);

		try
		{
			URI uri = new URI(url);

			RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).header("user", ua.getAccount().getUsername())
					.build();

			ResponseEntity<BankAccountDetailsView> res = restTemplate.exchange(rea, BankAccountDetailsView.class);

			bdv = res.getBody();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return bdv;

	}

	private String getPSD2Authorization()
	{
		if (psd2Authorization == null)
		{
			psd2Authorization = utils.createBase64AuthHeader(psd2Username, psd2Password);
		}
		return psd2Authorization;
	}

}
