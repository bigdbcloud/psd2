package com.ibm.api.cashew.services.barclays;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.barclays.Customer;
import com.ibm.api.cashew.beans.barclays.Transaction;
import com.ibm.api.cashew.utils.UUIDGenerator;
import com.ibm.psd2.datamodel.Amount;
import com.ibm.psd2.datamodel.Challenge;
import com.ibm.psd2.datamodel.aip.TransactionAccount;
import com.ibm.psd2.datamodel.aip.TransactionBank;
import com.ibm.psd2.datamodel.aip.TransactionDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

@Service
public class BarclaysServiceImpl implements BarclaysService {

	@Autowired
	RestTemplate restTemplate;

	@Value("${barclays.service.url}")
	private String barclaysUrl;

	@Value("${barclays.id}")
	private String barclaysBank;

	public Customer getUserAccounts(String customerId) throws URISyntaxException {

		String url = barclaysUrl + "/customers/{customerId}";

		URI uri = new URI(url);

		RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("customerId", customerId);
		Customer customer = restTemplate.getForObject(url, Customer.class, uriVariables);

		return customer;
	}

	public List<com.ibm.psd2.datamodel.aip.Transaction> getTransactions(String accountId) throws URISyntaxException {

		String url = barclaysUrl + "/accounts/" + accountId + "/transactions";
		URI uri = new URI(url);

		RequestEntity<Void> rea = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("accountId", accountId);

		ResponseEntity<List<Transaction>> txn = restTemplate.exchange(rea,
				new ParameterizedTypeReference<List<Transaction>>() {
				});

		List<Transaction> txnList = txn.getBody();

		List<com.ibm.psd2.datamodel.aip.Transaction> txnRes = null;

		if (!CollectionUtils.isEmpty(txn.getBody())) {

			txnRes = new ArrayList<com.ibm.psd2.datamodel.aip.Transaction>();
			for (Transaction transaction : txnList) {

				txnRes.add(populateTransactionRespose(transaction, accountId));
			}

		}

		return txnRes;
	}

	private com.ibm.psd2.datamodel.aip.Transaction populateTransactionRespose(Transaction transaction,
			String accountId) {

		com.ibm.psd2.datamodel.aip.Transaction txn = new com.ibm.psd2.datamodel.aip.Transaction();

		txn.setId(transaction.getId());
		TransactionDetails txnDetails = new TransactionDetails();
		Amount amt = new Amount();

		if (transaction.getAmount() != null) {

			double moneyIn = Double.valueOf(transaction.getAmount().getMoneyIn());
			double moneyOut = Double.valueOf(transaction.getAmount().getMoneyOut());

			if (moneyIn > 0.00) {

				amt.setAmount(moneyIn);
			} else {

				amt.setAmount(moneyOut);
			}

		}

		txnDetails.setValue(amt);
		Amount accBalance = new Amount();

		if (transaction.getAccountBalanceAfterTransaction() != null
				&& StringUtils.isNotBlank(transaction.getAccountBalanceAfterTransaction().getAmount())) {

			accBalance.setAmount(Double.valueOf(transaction.getAccountBalanceAfterTransaction().getAmount()));

		}

		if (StringUtils.isNotBlank(transaction.getCreated())) {

			txnDetails.setCompleted(com.ibm.psd2.datamodel.aip.Transaction.DATE_FORMAT.format(new Date()));
			txnDetails.setPosted(com.ibm.psd2.datamodel.aip.Transaction.DATE_FORMAT.format(new Date()));

		}

		txnDetails.setDescription(transaction.getDescription());
		txnDetails.setNewBalance(accBalance);
		txn.setDetails(txnDetails);

		TransactionBank txnBank = new TransactionBank();
		txnBank.setName(barclaysBank);
		txnBank.setNationalIdentifier(barclaysBank);

		TransactionAccount thisAcct = new TransactionAccount();
		thisAcct.setId(accountId);
		thisAcct.setBank(txnBank);

		txn.setThisAccount(thisAcct);

		TransactionAccount otherAcct = new TransactionAccount();
		otherAcct.setId(transaction.getPaymentDescriptor().getId());
		txn.setOtherAccount(otherAcct);

		return txn;
	}

	@Override
	public SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest) {

		Challenge challenge = new Challenge();
		challenge.setId(subscriptionRequest.getSubscriptionInfo().getBankId() + "-" + UUIDGenerator.generateUUID());
		challenge.setChallengeType(Challenge.ACCOUNT_SUBSCRIPTION);
		challenge.setAnswer(UUIDGenerator.generateUUID());
		challenge.setAllowedAttempts(Challenge.CHALLENGE_MAX_ATTEMPTS);

		subscriptionRequest.setChallenge(challenge);
		subscriptionRequest.setStatus(SubscriptionRequest.STATUS_SUBSCRIBED);

		subscriptionRequest.getSubscriptionInfo().setStatus(SubscriptionInfo.STATUS_ACTIVE);

		return subscriptionRequest;
	}

}
