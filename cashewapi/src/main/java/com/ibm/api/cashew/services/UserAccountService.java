package com.ibm.api.cashew.services;

import java.util.List;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface UserAccountService {
	public SubscriptionRequest subscribe(String username, SubscriptionRequest subscriptionRequest);

	public UserAccount answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca);

	public BankAccountDetailsView getAccountInformation(String appUser, String bankId, String accountId);

	public List<com.ibm.api.cashew.beans.Transaction> getTransactions(String appUser, String bankId, String accountId, String sortDirection,
			String fromDate, String toDate, String sortBy, Integer offset, Integer limit);

	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, String user);

	public List<BankAccountDetailsView> getAllAccountInformation(String appUser);

	

}
