package com.ibm.api.cashew.services.barclays;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.CounterParty;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface BarclaysService {

	List<Transaction> getTransactions(String accountId) throws URISyntaxException;

	SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest);

	Map<String, BankAccountDetailsView> getAccountInformation(UserAccount ua) throws URISyntaxException;

	List<CounterParty> getPayees(UserAccount ua) throws URISyntaxException;

}
