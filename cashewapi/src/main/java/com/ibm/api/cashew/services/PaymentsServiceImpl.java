package com.ibm.api.cashew.services;

import java.net.URI;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.db.mongo.MongoUserAccountsRepository;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

@Service
public class PaymentsServiceImpl implements PaymentsService
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

	@Value("${psd2.password}")
	private String psd2Password;

	private String psd2Authorization;

	private String getPSD2Authorization()
	{
		if (psd2Authorization == null)
		{
			psd2Authorization = utils.createBase64AuthHeader(psd2Username, psd2Password);
		}
		return psd2Authorization;
	}

	@Override
	public List<TransactionRequestType> getTransactionRequestTypes(String appUsername, String bankId, String accountId)
	{
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUsername, accountId, bankId);
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE))
		{
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		return ua.getTransactionRequestTypes();
	}
	
	@Override
	public TxnRequestDetails createTransactionRequest (String appUsername, String bankId, String accountId, TxnRequest trb)
	{
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUsername, accountId, bankId);
		
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE))
		{
			throw new IllegalArgumentException("Account is not yet subscribed");
		}
		
		if (trb == null || trb.getTo() == null || trb.getTo().getBankId() == null || trb.getTo().getAccountId() == null || trb.getValue() == null)
		{
			throw new IllegalArgumentException("Invalid Transaction Request");
		}
		
		/**
		 * banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests
		 */
		
		String txnType = null;
		TxnRequestDetails txnDetails = null;

		if (ua.getAccount().getBankId().equals(bankId))
		{
			txnType = TransactionRequestType.TYPES.WITHIN_BANK.name();
		}
		else
		{
			txnType = TransactionRequestType.TYPES.INTER_BANK.name();
		}
		
		String url = psd2Url + "/banks/" + ua.getAccount().getBankId() + "/accounts/" + ua.getAccount().getId() + "/"
				+ ua.getViewIds().get(0).getId() + "/transaction-request-types/" + txnType + "/transaction-requests";
		logger.debug("url = " + url);

		try
		{
			URI uri = new URI(url);

			RequestEntity<TxnRequest> rea = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
					.header("Authorization", getPSD2Authorization()).header("user", ua.getAccount().getUsername())
					.body(trb);

			ResponseEntity<TxnRequestDetails> res = restTemplate.exchange(rea, TxnRequestDetails.class);

			txnDetails = res.getBody();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return txnDetails;
	}
	
	
}
