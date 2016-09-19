package com.ibm.api.cashew.bank.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.bank.db.MongoTransactionRepository;
import com.ibm.api.cashew.elastic.db.ElasticTransactionRepository;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;

@Service
public class BankServiceImpl implements BankService {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private Utils utils;

	@Value("${psd2.url}")
	private String psd2Url;

	@Value("${psd2.username}")
	private String psd2Username;

	@Value("${psd2.password}")
	private String psd2Password;

	@Autowired
	private MongoTransactionRepository mongoTxnRepo;

	@Autowired
	private ElasticTransactionRepository elasticTxnRepo;

	@Override
	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, String user) {

		String url = psd2Url
				+ "/banks/{bankId}/accounts/{accountId}/{viewId}/transaction-request-types/{txnType}/transaction-requests";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", utils.createBase64AuthHeader(psd2Username, psd2Password));
		headers.add("Content-Type", "application/json");
		headers.add("user", user);

		HttpEntity<TxnRequest> request = new HttpEntity<TxnRequest>(txnReq, headers);

		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("bankId", payer.getBankId());
		uriVariables.put("accountId", payer.getAccountId());
		uriVariables.put("viewId", "owner");
		uriVariables.put("txnType", txnType);

		return restTemplate.postForObject(url, request, TxnRequestDetails.class, uriVariables);

	}

	@Override
	public List<Transaction> getTransactions(String userId, String accountId, String bankId, String fromDate,
			String toDate, String sortBy, Integer offset, Integer limit) {

		String url = psd2Url + "/banks/{bankId}/accounts/{accountId}/{viewId}/transactions";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", utils.createBase64AuthHeader(psd2Username, psd2Password));
		headers.add("Content-Type", "application/json");
		headers.add("obp_limit", limit.toString());
		headers.add("obp_from_date", fromDate);
		headers.add("obp_to_date", toDate);
		headers.add("obp_sort_by", sortBy);
		headers.add("obp_offset", offset.toString());
		headers.add("user", userId);

		HttpEntity request = new HttpEntity(headers);

		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("bankId", bankId);
		uriVariables.put("accountId", accountId);
		uriVariables.put("viewId", "owner");
		ResponseEntity<List<Transaction>> response = restTemplate.exchange(url, HttpMethod.GET, request,
				new ParameterizedTypeReference<List<Transaction>>() {
				}, uriVariables);

		// save data in mongo and elastic search

		if (!CollectionUtils.isEmpty(response.getBody())) {

			mongoTxnRepo.save(response.getBody());
			elasticTxnRepo.save(populateElasticTxnDetails(response.getBody()));
		}

		return response.getBody();

	}

	private List<com.ibm.api.cashew.elastic.beans.Transaction> populateElasticTxnDetails(List<Transaction> txnList) {

		List<com.ibm.api.cashew.elastic.beans.Transaction> elasticTxnList = new ArrayList<com.ibm.api.cashew.elastic.beans.Transaction>();

		for (Transaction txn : txnList) {
			com.ibm.api.cashew.elastic.beans.Transaction elasticTxn = new com.ibm.api.cashew.elastic.beans.Transaction();
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
	public Transaction tagTransaction(String bankId, String accountId, String txnId, String tag) {

		String url = psd2Url + "/banks/{bankId}/accounts/{accountId}/{viewId}/transaction/{txnId}/tag/{tag}";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Authorization", utils.createBase64AuthHeader(psd2Username, psd2Password));
		headers.add("Content-Type", "application/json");

		HttpEntity request = new HttpEntity(headers);

		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("bankId", bankId);
		uriVariables.put("accountId", accountId);
		uriVariables.put("viewId", "owner");
		uriVariables.put("txnId", txnId);
		uriVariables.put("tag", tag);
		
		ResponseEntity<Transaction> response = restTemplate.exchange(url, HttpMethod.PATCH, request,Transaction.class, uriVariables);
		
		return response.getBody();


	}

}
