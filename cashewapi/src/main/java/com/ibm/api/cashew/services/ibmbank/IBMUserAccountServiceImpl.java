package com.ibm.api.cashew.services.ibmbank;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@Service
public class IBMUserAccountServiceImpl implements IBMUserAccountService
{
	private Logger logger = LogManager.getLogger(IBMUserAccountServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	IBMPSD2Credentials psd2Credentials;

	@Override
	public SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest) throws Exception
	{
		SubscriptionRequest res = null;
		logger.debug("subscribing to username = " + subscriptionRequest);

		String url = psd2Credentials.getPsd2Url() + "/subscriptionRequest";

		URI uri = new URI(url);
		RequestEntity<SubscriptionRequest> re = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", psd2Credentials.getPSD2Authorization()).body(subscriptionRequest);
		ResponseEntity<SubscriptionRequest> response = restTemplate.exchange(re, SubscriptionRequest.class);
		res = response.getBody();

		return res;
	}

	@Override
	public SubscriptionInfo answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca) throws Exception
	{
		SubscriptionInfo si = null;
		String url = psd2Credentials.getPsd2Url() + "/subscription/" + sca.getSubscriptionRequestId();
		logger.debug("url = " + url);
		URI uri = new URI(url);
		RequestEntity<ChallengeAnswer> re = RequestEntity.patch(uri).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", psd2Credentials.getPSD2Authorization()).body(sca.getChallengeAnswer());
		ResponseEntity<SubscriptionInfo> response = restTemplate.exchange(re, SubscriptionInfo.class);
		si = response.getBody();
		return si;
	}

	@Override
	public BankAccountDetailsView getAccountInformation(UserAccount ua) throws Exception
	{
		BankAccountDetailsView bdv = null;
		String url = psd2Credentials.getPsd2Url() + "/banks/" + ua.getAccount().getBankId() + "/accounts/" + ua.getAccount().getId() + "/"
				+ ua.getViewIds().get(0).getId() + "/account";
		logger.debug("url = " + url);

		URI uri = new URI(url);

		RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", psd2Credentials.getPSD2Authorization()).header("user", ua.getAccount().getUsername()).build();

		ResponseEntity<BankAccountDetailsView> res = restTemplate.exchange(rea, BankAccountDetailsView.class);
		bdv = res.getBody();
		return bdv;
	}

	@Override
	public List<Transaction> getTransactions(UserAccount ua, String sortDirection, String fromDate, String toDate,
			String sortBy, Integer offset, Integer limit) throws Exception
	{
		List<Transaction> txns = null;

		String url = psd2Credentials.getPsd2Url() + "/banks/" + ua.getAccount().getBankId() + "/accounts/" + ua.getAccount().getId() + "/"
				+ ua.getViewIds().get(0).getId() + "/transactions";

		URI uri = new URI(url);
		logger.debug("url = " + url);
		
		RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", psd2Credentials.getPSD2Authorization()).header("user", ua.getAccount().getUsername())
				.header("obp_sort_direction", sortDirection).header("obp_from_date", fromDate)
				.header("obp_to_date", toDate).header("obp_sort_by", sortBy)
				.header("obp_offset", (offset == null) ? "0" : offset.toString())
				.header("obp_limit", (limit == null) ? "100" : offset.toString()).build();

		ResponseEntity<List<Transaction>> res = restTemplate.exchange(rea,
				new ParameterizedTypeReference<List<Transaction>>()
				{
				});
		txns = res.getBody();
		return txns;
	}

	@Override
	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, UserAccount ua) throws Exception
	{
		TxnRequestDetails txnReqDetails = null;
			String url = psd2Credentials.getPsd2Url()
					+ "/banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests";

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
			headers.add("Authorization", psd2Credentials.getPSD2Authorization());
			headers.add("Content-Type", "application/json");
			headers.add("user", ua.getAccount().getUsername());

			HttpEntity<TxnRequest> request = new HttpEntity<TxnRequest>(txnReq, headers);

			Map<String, String> uriVariables = new HashMap<String, String>();
			uriVariables.put("bankId", payer.getBankId());
			uriVariables.put("accountId", payer.getAccountId());
			uriVariables.put("viewId", ua.getViewIds().get(0).getId());
			uriVariables.put("txnType", txnType);
			txnReqDetails = restTemplate.postForObject(url, request, TxnRequestDetails.class, uriVariables);

		return txnReqDetails;
	}

}
