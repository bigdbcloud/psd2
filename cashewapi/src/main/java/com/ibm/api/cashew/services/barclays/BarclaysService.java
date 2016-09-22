package com.ibm.api.cashew.services.barclays;

import java.net.URISyntaxException;
import java.util.List;

import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface BarclaysService {

	List<Transaction> getTransactions(String accountId) throws URISyntaxException;

	SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest);

	

}
