package com.ibm.api.cashew.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.db.elastic.ElasticTransactionRepository;
import com.ibm.api.cashew.db.mongo.MongoUserAccountsRepository;
import com.ibm.api.cashew.services.barclays.BarclaysService;
import com.ibm.api.cashew.services.ibmbank.IBMPaymentsService;
import com.ibm.psd2.datamodel.aip.TransactionDetails;
import com.ibm.psd2.datamodel.pisp.CounterParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

@Service
public class PaymentsServiceImpl implements PaymentsService {
	private Logger logger = LogManager.getLogger(PaymentsServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	MongoUserAccountsRepository muar;

	@Autowired
	IBMPaymentsService ibmPaymentsService;

	@Autowired
	private ElasticTransactionRepository elasticTxnRepo;

	@Value("${ibmbank.id}")
	private String ibmBank;

	@Value("${barclays.id}")
	private String barclaysBank;

	@Autowired
	private BarclaysService barclaysService;

	@Override
	public List<TransactionRequestType> getTransactionRequestTypes(String appUsername, String bankId,
			String accountId) {
		logger.debug("parameters to useraccount are: " + appUsername + ", " + accountId + ", " + bankId);

		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUsername, accountId, bankId);
		logger.debug("ua = " + ua);
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE)) {
			throw new IllegalArgumentException("Account is not yet subscribed");
		}
		return ua.getTransactionRequestTypes();
	}

	@Override
	public TxnRequestDetails createTransactionRequest(String appUsername, String bankId, String accountId,
			TxnRequest trb) {
		logger.debug("parameters to useraccount are: " + appUsername + ", " + accountId + ", " + bankId);
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUsername, accountId, bankId);
		logger.debug("ua = " + ua);

		TxnRequestDetails txnDetails = null;

		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE)) {
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		if (trb == null || trb.getTo() == null || trb.getTo().getBankId() == null || trb.getTo().getAccountId() == null
				|| trb.getValue() == null) {
			throw new IllegalArgumentException("Invalid Transaction Request");
		}

		if (trb.getTransactionRequestType() == null || trb.getTransactionRequestType().isEmpty()
				|| !TransactionRequestType.isValid(trb.getTransactionRequestType())) {
			throw new IllegalArgumentException("Invalid Transaction Type specified");
		}

		/**
		 * check if the txn is for a self account. This is not a fool proof
		 * check.. ideally check has to be on the app.
		 */
		UserAccount toAccount = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUsername,
				trb.getTo().getAccountId(), trb.getTo().getBankId());

		if (toAccount != null) {
			trb.setTransactionRequestType(TransactionRequestType.TYPES.SELF.type());
		}

		try {
			if (ua.getAccount().getBankId().equals(ibmBank)) {
				txnDetails = ibmPaymentsService.createTransactionRequest(ua, trb, trb.getTransactionRequestType());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return txnDetails;
	}

	@Override
	public com.ibm.api.cashew.beans.Transaction tagTransaction(String userId, String bankId, String accountId,
			String txnId, String tag) {
		com.ibm.api.cashew.beans.Transaction txn = elasticTxnRepo.findOne(txnId);

		if (txn == null) {
			throw new IllegalArgumentException("Transaction details doesn't exist");
		}

		if (txn.getDetails() == null) {

			txn.setDetails(new TransactionDetails());
		}

		txn.getDetails().setTag(tag);
		return elasticTxnRepo.save(txn);
	}

	@Override
	public List<CounterParty> getPayees(String appUsername, String bankId, String accountId) {
		List<CounterParty> payees = null;
		logger.debug("parameters to useraccount are: " + appUsername + ", " + accountId + ", " + bankId);
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUsername, accountId, bankId);
		logger.debug("ua = " + ua);

		try {
			if (ua != null) {
				if (ua.getAccount().getBankId().equals(ibmBank)) {
					payees = ibmPaymentsService.getPayees(ua);
				}

				if (ua.getAccount().getBankId().equals(barclaysBank)) {
					payees = barclaysService.getPayees(ua);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return payees;
	}

}
