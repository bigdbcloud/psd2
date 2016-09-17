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

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService
{
	private Logger logger = LogManager.getLogger(UserSubscriptionServiceImpl.class);

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

	@Value("${psd2.password}")
	private String psd2Password;

	@Override
	public SubscriptionRequest subscribe(UserAccount userAccount)
	{
		SubscriptionRequest res = null;
		logger.debug("subscribing to account = " + userAccount);
		try
		{
			/*
			 * First save the account in local DB Then make a subscription
			 * request call
			 */
			String url = psd2Url + "/subscription";

			URI uri = new URI(url);
			RequestEntity<SubscriptionRequest> re = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", utils.createBase64AuthHeader(psd2Username, psd2Password))
					.body(userAccount.getSubscriptionRequest());
			ResponseEntity<SubscriptionRequest> response = restTemplate.exchange(re, SubscriptionRequest.class);
			res = response.getBody();
			userAccount.setSubscriptionRequest(res);
			muar.save(userAccount);
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
			String authorization = utils.createBase64AuthHeader(psd2Username, psd2Password);
			String url = psd2Url + "/subscription" + sca.getSubscriptionRequestId();
			URI uri = new URI(url);
			RequestEntity<ChallengeAnswer> re = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", authorization).body(sca.getChallengeAnswer());
			ResponseEntity<SubscriptionInfo> response = restTemplate.exchange(re, SubscriptionInfo.class);
			SubscriptionInfo si = response.getBody();

			if (si != null)
			{
				url = psd2Url + "/banks/" + si.getBankId() + "/accounts/" + si.getAccountId() + "/" + si.getViewIds()
						+ "/account";
				uri = new URI(url);

				RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
						.header("Authorization", authorization).build();
				ResponseEntity<BankAccountDetailsView> res = restTemplate.exchange(rea, BankAccountDetailsView.class);

				BankAccountDetailsView bdv = res.getBody();
				if (bdv != null)
				{

					ua = muar.findByAppUsernameAndAccountIdAndAccountBankIdAndAccountUsername(sca.getAppUsername(),
							si.getAccountId(), si.getBankId(), si.getUsername());
					ua.setSubscription(si);
					ua.setAccount(bdv);
					ua.getAccount().setBalance(null);
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
}
