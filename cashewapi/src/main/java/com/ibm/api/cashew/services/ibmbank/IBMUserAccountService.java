package com.ibm.api.cashew.services.ibmbank;

import java.util.List;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;

public interface IBMUserAccountService
{
	public SubscriptionRequest subscribe(SubscriptionRequest subscriptionRequest) throws Exception;

	public SubscriptionInfo answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca) throws Exception;

	public BankAccountDetailsView getAccountInformation(UserAccount ua) throws Exception;

	public List<Transaction> getTransactions(UserAccount ua, String sortDirection, String fromDate, String toDate,
			String sortBy, Integer offset, Integer limit) throws Exception;

	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, UserAccount ua)
			throws Exception;

}
