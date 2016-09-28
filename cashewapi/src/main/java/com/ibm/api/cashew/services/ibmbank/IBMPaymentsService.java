package com.ibm.api.cashew.services.ibmbank;

import java.net.URISyntaxException;
import java.util.List;

import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.pisp.CounterParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;

public interface IBMPaymentsService
{
	public TxnRequestDetails createTransactionRequest(UserAccount ua, TxnRequest trb, String txnType) throws Exception;

	public List<CounterParty> getPayees(UserAccount ua) throws Exception;

	public TxnRequestDetails answerTxnChallenge(UserAccount ua, String txnReqType, String txnId, ChallengeAnswer ca)
			throws URISyntaxException;

}
