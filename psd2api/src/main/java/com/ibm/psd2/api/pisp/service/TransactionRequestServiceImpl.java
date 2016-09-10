package com.ibm.psd2.api.pisp.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ibm.psd2.api.pisp.db.MongoTxnRequestDetailsRepository;
import com.ibm.psd2.api.pisp.rules.PaymentRules;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.commons.datamodel.Challenge;
import com.ibm.psd2.commons.datamodel.ChallengeAnswer;
import com.ibm.psd2.commons.datamodel.pisp.TxnParty;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequest;
import com.ibm.psd2.commons.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.utils.UUIDGenerator;

@Service
public class TransactionRequestServiceImpl implements TransactionRequestService
{
	private final Logger logger = LogManager.getLogger(TransactionRequestServiceImpl.class);

	@Autowired
	private PaymentRules pr;

	@Autowired
	private MongoTxnRequestDetailsRepository mtrdr;

	@Override
	public TxnRequestDetails createTransactionRequest(SubscriptionInfo sib, TxnRequest trb, TxnParty payee,
			String txnType) throws Exception
	{
		if (!pr.isTransactionTypeAllowed(sib, txnType))
		{
			throw new IllegalArgumentException("Invalid Transaction Type Specified. Available Values are: "
					+ StringUtils.collectionToCommaDelimitedString(sib.getTransactionRequestTypes()));
		}

		TxnRequestDetails txnRequest = new TxnRequestDetails();
		txnRequest.setBody(trb);
		txnRequest.setType(txnType);

		if (pr.checkLimit(trb, sib, txnType))
		{
			logger.info("Within Specified Limit. Hence Not Challenging the Request");
			txnRequest.setChallenge(null);
			txnRequest.setStatus(TxnRequestDetails.TXN_STATUS_PENDING);
		}
		else
		{
			logger.info("Amount Greater than specified limit. Hence creating a challenge");
			Challenge challenge = new Challenge();
			challenge.setId(UUIDGenerator.generateUUID());
			challenge.setChallenge_type(txnRequest.getType());
			challenge.setAllowed_attempts(Constants.CHALLENGE_MAX_ATTEMPTS);
			txnRequest.setChallenge(challenge);
			txnRequest.setStatus(TxnRequestDetails.TXN_STATUS_INITIATED);
		}

		txnRequest.setTransactionIds(UUIDGenerator.generateUUID());
		txnRequest.setFrom(payee);
		txnRequest.setStartDate(new Date());
		txnRequest.setId(UUIDGenerator.generateUUID());
		txnRequest.setCharge(pr.getTransactionCharge(trb, payee));

		return mtrdr.save(txnRequest);
	}

	@Override
	public TxnRequestDetails answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswer t) throws Exception
	{
		TxnRequestDetails tdb = mtrdr.findByFromAccountIdAndFromBankIdAndTypeAndIdAndChallengeId(accountId, bankId,
				txnType, txnReqId, t.getId());
		if (tdb == null)
		{
			throw new IllegalArgumentException("Specified Transaction Not Found");
		}

		if (!pr.validateTxnChallengeAnswer(t, username, accountId, bankId))
		{
			throw new IllegalArgumentException("Incorrect Transaction Request Challenge Answer Specified");
		}

		tdb.setStatus(TxnRequestDetails.TXN_STATUS_PENDING);
		return mtrdr.save(tdb);
	}

	@Override
	public List<TxnRequestDetails> getTransactionRequests(String username, String viewId, String accountId,
			String bankId) throws Exception
	{
		return mtrdr.findByFromAccountIdAndFromBankId(accountId, bankId);
	}
}
