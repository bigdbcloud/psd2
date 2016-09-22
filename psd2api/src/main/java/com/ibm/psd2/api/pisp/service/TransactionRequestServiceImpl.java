package com.ibm.psd2.api.pisp.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ibm.psd2.api.aip.services.BankAccountDetailsService;
import com.ibm.psd2.api.aip.services.TransactionStatementService;
import com.ibm.psd2.api.pisp.db.MongoTxnRequestDetailsRepository;
import com.ibm.psd2.api.utils.Constants;
import com.ibm.psd2.datamodel.Amount;
import com.ibm.psd2.datamodel.Challenge;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.aip.TransactionAccount;
import com.ibm.psd2.datamodel.aip.TransactionBank;
import com.ibm.psd2.datamodel.aip.TransactionDetails;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.utils.UUIDGenerator;

@Service
public class TransactionRequestServiceImpl implements TransactionRequestService
{
	private final Logger logger = LogManager.getLogger(TransactionRequestServiceImpl.class);

	@Autowired
	private PaymentRules pr;

	@Autowired
	private MongoTxnRequestDetailsRepository mtrdr;

	@Autowired
	private TransactionStatementService tss;

	@Autowired
	private BankAccountDetailsService bds;

	private void postTransactionToBeneficiary(TxnRequestDetails txnReq)
	{
		logger.info("Processing Beneficiary txnRequest: " + txnReq);
		
		BankAccountDetails to = bds.getBankAccountDetails(txnReq.getBody().getTo().getBankId(), txnReq.getBody().getTo().getAccountId());
		double newToBalance = 0;
		if (to!= null)
		{
			newToBalance = to.getBalance().getAmount() + txnReq.getBody().getValue().getAmount();
			to.getBalance().setAmount(newToBalance);
			bds.updateBalance(to.getBankId(), to.getId(), newToBalance);

			Transaction txn = new Transaction();

			txn.setId(UUIDGenerator.generateUUID());
			
			TransactionDetails txnDetails = new TransactionDetails();
			txnDetails.setCompleted(Transaction.DATE_FORMAT.format(new Date()));
			txnDetails.setDescription(txnReq.getBody().getDescription());
			txnDetails.setNewBalance(to.getBalance());
			txnDetails.setPosted(Transaction.DATE_FORMAT.format(new Date()));
			txnDetails.setType(txnReq.getType());
			txnDetails.setValue(txnReq.getBody().getValue());

			txn.setDetails(txnDetails);
			

			TransactionAccount thisAcc = new TransactionAccount();

			thisAcc.setId(to.getId());

			TransactionBank beneficiaryBank = new TransactionBank();
			beneficiaryBank.setName(to.getBankId());
			beneficiaryBank.setNationalIdentifier(to.getBankId());
			thisAcc.setBank(beneficiaryBank);

			thisAcc.setHolders(to.getOwners());
			thisAcc.setIban(to.getIban());
			thisAcc.setNumber(to.getNumber());
			thisAcc.setSwiftBic(to.getSwiftBic());

			txn.setThisAccount(thisAcc);
			
			TransactionAccount toAcc = new TransactionAccount();

			toAcc.setId(txnReq.getFrom().getAccountId());

			TransactionBank payerBank = new TransactionBank();
			payerBank.setName(txnReq.getFrom().getBankId());
			payerBank.setNationalIdentifier(txnReq.getFrom().getBankId());
			toAcc.setBank(payerBank);

			txn.setOtherAccount(toAcc);

			logger.info("Saving Beneficiary Transaction = " + txn);
			
			tss.createTransaction(txn);
		}
	}

	private void postTransactionToPayer(TxnRequestDetails txnReq)
	{
		logger.info("Processing Payer txnRequest: " + txnReq);

		Transaction txn = new Transaction();

		txn.setId(txnReq.getTransactionIds());
		BankAccountDetails from = bds.getBankAccountDetails(txnReq.getFrom().getBankId(),
				txnReq.getFrom().getAccountId());
		
		double newFromBalance = from.getBalance().getAmount() - txnReq.getBody().getValue().getAmount()
				- txnReq.getCharge().getValue().getAmount();
		
		bds.updateBalance(from.getBankId(), from.getId(), newFromBalance);
		
		from.getBalance().setAmount(newFromBalance);

		TransactionDetails txnDetails = new TransactionDetails();
		txnDetails.setCompleted(Transaction.DATE_FORMAT.format(new Date()));
		txnDetails.setDescription(txnReq.getBody().getDescription());
		txnDetails.setNewBalance(from.getBalance());
		txnDetails.setPosted(Transaction.DATE_FORMAT.format(new Date()));
		txnDetails.setType(txnReq.getType());
		Amount value = txnReq.getBody().getValue();
		value.setAmount(value.getAmount()*(-1));
		txnDetails.setValue(value);

		txn.setDetails(txnDetails);

		TransactionAccount thisAcc = new TransactionAccount();
		thisAcc.setId(from.getId());

		TransactionBank tbb = new TransactionBank();
		tbb.setName(from.getBankId());
		tbb.setNationalIdentifier(from.getBankId());
		thisAcc.setBank(tbb);

		thisAcc.setHolders(from.getOwners());
		thisAcc.setIban(from.getIban());
		thisAcc.setNumber(from.getNumber());
		thisAcc.setSwiftBic(from.getSwiftBic());

		txn.setThisAccount(thisAcc);

		TransactionAccount toAcc = new TransactionAccount();
		toAcc.setId(txnReq.getBody().getTo().getAccountId());

		TransactionBank tbb1 = new TransactionBank();
		tbb1.setNationalIdentifier(txnReq.getBody().getTo().getBankId());
		tbb1.setName(txnReq.getBody().getTo().getBankId());
		toAcc.setBank(tbb1);

		txn.setOtherAccount(toAcc);

		logger.info("Saving Payer Transaction = " + txn);

		tss.createTransaction(txn);
	}

	@Override
	public TxnRequestDetails createTransactionRequest(SubscriptionInfo sib, TxnRequest trb, TxnParty payee,
			String txnType)
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
			challenge.setChallengeType(txnRequest.getType());
			challenge.setAllowedAttempts(Constants.CHALLENGE_MAX_ATTEMPTS);
			txnRequest.setChallenge(challenge);
			txnRequest.setStatus(TxnRequestDetails.TXN_STATUS_INITIATED);
		}

		txnRequest.setTransactionIds(UUIDGenerator.generateUUID());
		txnRequest.setFrom(payee);
		txnRequest.setStartDate(TxnRequest.DATE_FORMAT.format(new Date()));
		txnRequest.setId(UUIDGenerator.generateUUID());
		txnRequest.setCharge(pr.getTransactionCharge(trb, payee));

		TxnRequestDetails savedTxn = mtrdr.save(txnRequest);

		// Hack to bypass Txn Processing
		if (savedTxn.getStatus().equals(TxnRequestDetails.TXN_STATUS_PENDING))
		{
			postTransactionToBeneficiary(savedTxn);
			postTransactionToPayer(savedTxn);
			savedTxn.setStatus(TxnRequestDetails.TXN_STATUS_COMPLETED);
			savedTxn.setEndDate(TxnRequest.DATE_FORMAT.format(new Date()));
		}
		return mtrdr.save(savedTxn);
	}

	@Override
	public TxnRequestDetails answerTransactionRequestChallenge(String username, String viewId, String bankId,
			String accountId, String txnType, String txnReqId, ChallengeAnswer t)
	{
//		TxnRequestDetails tdb = mtrdr.findByFromAccountIdAndFromBankIdAndTypeAndIdAndChallengeId(accountId, bankId,
//				txnType, txnReqId, t.getId());
		TxnRequestDetails tdb = mtrdr.findOne(txnReqId);
		if (tdb == null)
		{
			throw new IllegalArgumentException("Specified Transaction Not Found");
		}

		if (!pr.validateTxnChallengeAnswer(t, username, accountId, bankId))
		{
			throw new IllegalArgumentException("Incorrect Transaction Request Challenge Answer Specified");
		}

		tdb.setStatus(TxnRequestDetails.TXN_STATUS_PENDING);
		TxnRequestDetails savedTxn = mtrdr.save(tdb);

		// Hack to bypass Txn Processing
		if (savedTxn.getStatus().equals(TxnRequestDetails.TXN_STATUS_PENDING))
		{
			postTransactionToBeneficiary(savedTxn);
			postTransactionToPayer(savedTxn);
			savedTxn.setStatus(TxnRequestDetails.TXN_STATUS_COMPLETED);
			savedTxn.setEndDate(TxnRequest.DATE_FORMAT.format(new Date()));
		}
		return mtrdr.save(savedTxn);
	}

	@Override
	public List<TxnRequestDetails> getTransactionRequests(String username, String viewId, String accountId,
			String bankId)
	{
		return mtrdr.findByFromAccountIdAndFromBankId(accountId, bankId);
	}
}
