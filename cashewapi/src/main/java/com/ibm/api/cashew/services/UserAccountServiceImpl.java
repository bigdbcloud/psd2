package com.ibm.api.cashew.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.SubscriptionChallengeAnswer;
import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.api.cashew.db.elastic.ElasticTransactionRepository;
import com.ibm.api.cashew.db.mongo.MongoUserAccountsRepository;
import com.ibm.api.cashew.services.barclays.BarclaysService;
import com.ibm.api.cashew.services.ibmbank.IBMUserAccountService;
import com.ibm.psd2.datamodel.aip.BankAccountDetailsView;
import com.ibm.psd2.datamodel.aip.Transaction;
import com.ibm.psd2.datamodel.aip.TransactionDetails;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;
import com.ibm.psd2.utils.UUIDGenerator;

@Service
public class UserAccountServiceImpl implements UserAccountService
{
	private Logger logger = LogManager.getLogger(UserAccountServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	MongoUserAccountsRepository muar;

	@Autowired
	IBMUserAccountService ibmUserAccSvc;

	@Value("${ibmbank.id}")
	private String ibmBank;

	@Value("${barclays.id}")
	private String barclaysBank;

	@Autowired
	private BarclaysService barclaysService;

	@Autowired
	private ElasticTransactionRepository elasticTxnRepo;

	@Override
	public SubscriptionRequest subscribe(String username, SubscriptionRequest subscriptionRequest)
	{
		SubscriptionRequest res = null;
		logger.debug("subscribing to username = " + username);
		try
		{

			UserAccount ua = muar.findByAccountIdAndAccountBankId(
					subscriptionRequest.getSubscriptionInfo().getAccountId(),
					subscriptionRequest.getSubscriptionInfo().getBankId());

			if (ua != null)
			{
				throw new IllegalArgumentException("Account Already Subscribed");
			}

			if (subscriptionRequest.getSubscriptionInfo().getBankId().equals(ibmBank))
			{
				res = ibmUserAccSvc.subscribe(subscriptionRequest);
			}

			if (subscriptionRequest.getSubscriptionInfo().getBankId().equals(barclaysBank))
			{
				res = barclaysService.subscribe(subscriptionRequest);
			}

			ua = new UserAccount();
			ua.setId(UUIDGenerator.generateUUID());
			ua.setAppUsername(username);
			ua.setSubscriptionRequestId(res.getId());
			ua.setSubscriptionRequestStatus(res.getStatus());
			ua.setViewIds(res.getSubscriptionInfo().getViewIds());
			ua.setLimits(res.getSubscriptionInfo().getLimits());
			ua.setTransactionRequestTypes(res.getSubscriptionInfo().getTransactionRequestTypes());
			ua.setSubscriptionRequestChallengeId(res.getChallenge().getId());
			
			if (res.getSubscriptionInfo() != null)
			{
				ua.setSubscriptionInfoStatus(res.getSubscriptionInfo().getStatus());
			}

			BankAccountDetailsView badv = new BankAccountDetailsView();
			badv.setBankId(res.getSubscriptionInfo().getBankId());
			badv.setUsername(res.getSubscriptionInfo().getUsername());
			badv.setId(res.getSubscriptionInfo().getAccountId());
			ua.setAccount(badv);
			muar.save(ua);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return res;
	}

	@Override
	public UserAccount answerSubscriptionRequestChallenge(SubscriptionChallengeAnswer sca)
	{
		/**
		 * first answer challenge of PSD2API /subscription/{id} once
		 * subscription request completes. call the getAccountAPI to
		 * getAccountInformation
		 * /banks/{bankId}/accounts/{accountId}/{viewId}/account
		 */
		UserAccount ua = null;
		try
		{

			ua = muar.findBySubscriptionRequestId(sca.getSubscriptionRequestId());
			logger.debug("Found UserAccount = " + ua);

			if (ua == null || !ua.getAppUsername().equals(sca.getAppUsername()))
			{
				throw new IllegalArgumentException("Invalid Subscription Challenge Answer Specified");
			}

			SubscriptionInfo si = null;
			if (ua.getAccount().getBankId().equals(ibmBank))
			{
				si = ibmUserAccSvc.answerSubscriptionRequestChallenge(sca);
			}

			if (si != null)
			{

				BankAccountDetailsView bdv = ibmUserAccSvc.getAccountInformation(ua);
				if (ua.getAccount().getBankId().equals(ibmBank))
				{
					bdv = ibmUserAccSvc.getAccountInformation(ua);
				}
				if (bdv != null)
				{
					logger.debug("parameters to useraccount are: " + sca.getAppUsername() + ", " + si.getAccountId()
							+ ", " + si.getBankId() + ", " + si.getUsername());
					ua = muar.findByAppUsernameAndAccountIdAndAccountBankIdAndAccountUsername(sca.getAppUsername(),
							si.getAccountId(), si.getBankId(), si.getUsername());

					ua.setAccount(bdv);
					ua.getAccount().setBalance(null);
					ua.setSubscriptionInfoStatus(si.getStatus());
					muar.save(ua);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return ua;
	}

	@Override
	public BankAccountDetailsView getAccountInformation(String appUser, String bankId, String accountId)
	{
		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(appUser, accountId, bankId);
		logger.debug("Found UserAccount = " + ua);

		BankAccountDetailsView bdv = null;
		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE))
		{
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		try
		{
			if (ua.getAccount().getBankId().equals(ibmBank))
			{
				bdv = ibmUserAccSvc.getAccountInformation(ua);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return bdv;

	}

	@Override
	public List<BankAccountDetailsView> getAllAccountInformation(String appUser)
	{
		List<UserAccount> uas = muar.findByAppUsername(appUser);
		logger.debug("Found UserAccount = " + uas);

		List<BankAccountDetailsView> bdvs = null;
		Map<String, BankAccountDetailsView> barclaysadv = null;

		try
		{
			if (uas != null)
			{

				for (Iterator<UserAccount> iterator = uas.iterator(); iterator.hasNext();)
				{
					UserAccount ua = iterator.next();

					if (!SubscriptionInfo.STATUS_ACTIVE.equals(ua.getSubscriptionInfoStatus()))
					{
						continue;
					}

					BankAccountDetailsView badv = null;
					if (ua.getAccount().getBankId().equals(ibmBank))
					{
						badv = ibmUserAccSvc.getAccountInformation(ua);
					}

					if (ua.getAccount().getBankId().equals(barclaysBank))
					{
						if (barclaysadv == null)
						{
							barclaysadv = barclaysService.getAccountInformation(ua);
						}

						if (!CollectionUtils.isEmpty(barclaysadv))
						{
							badv = barclaysadv.get(ua.getAccount().getId());
						}
					}

					if (badv != null)
					{
						if (bdvs == null)
						{
							bdvs = new ArrayList<>();
						}
						bdvs.add(badv);
					}

				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return bdvs;
	}

	@Override
	public List<com.ibm.api.cashew.beans.Transaction> getTransactions(String appUser, String bankId, String accountId,
			String sortDirection, String fromDate, String toDate, String sortBy, Integer offset, Integer limit)
	{

		List<UserAccount> uas = muar.findByAppUsername(appUser);
		List<Transaction> txns = new ArrayList<Transaction>();

		if (uas != null)
		{

			for (Iterator<UserAccount> iterator = uas.iterator(); iterator.hasNext();)
			{
				UserAccount ua = iterator.next();

				if (!SubscriptionInfo.STATUS_ACTIVE.equals(ua.getSubscriptionInfoStatus()))
				{
					continue;
				}

				try
				{
					if (ua.getAccount().getBankId().equals(ibmBank))
					{
						txns.addAll(ibmUserAccSvc.getTransactions(ua, sortDirection, fromDate, toDate, sortBy, offset,
								limit));
					}

					if (ua.getAccount().getBankId().equals(barclaysBank))
					{
						txns.addAll(barclaysService.getTransactions(accountId));
					}

				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}

			}
		}

		// save data in mongo and elastic search

		if (!CollectionUtils.isEmpty(txns))
		{

			List<com.ibm.api.cashew.beans.Transaction> txnList = populateElasticTxnDetails(txns, appUser);
			if (!CollectionUtils.isEmpty(txnList))
			{
				elasticTxnRepo.save(txnList);
			}
		}

		return elasticTxnRepo.getTransactions(appUser, bankId, accountId, fromDate, toDate);
	}

	@Override
	public TxnRequestDetails createTransaction(TxnRequest txnReq, TxnParty payer, String txnType, String user)
	{

		UserAccount ua = muar.findByAppUsernameAndAccountIdAndAccountBankId(user, payer.getAccountId(),
				payer.getBankId());
		logger.debug("Found UserAccount = " + ua);

		TxnRequestDetails txnReqDetails = null;

		if (ua == null || ua.getSubscriptionInfoStatus() == null
				|| !ua.getSubscriptionInfoStatus().equals(SubscriptionInfo.STATUS_ACTIVE))
		{
			throw new IllegalArgumentException("Account is not yet subscribed");
		}

		try
		{
			if (ua.getAccount().getBankId().equals(ibmBank))
			{
				txnReqDetails = ibmUserAccSvc.createTransaction(txnReq, payer, txnType, ua);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}

		return txnReqDetails;
	}

	private List<com.ibm.api.cashew.beans.Transaction> populateElasticTxnDetails(List<Transaction> txnList,
			String appUser)
	{

		List<com.ibm.api.cashew.beans.Transaction> elasticTxnList = new ArrayList<com.ibm.api.cashew.beans.Transaction>();
		for (Transaction txn : txnList)
		{

			if (elasticTxnRepo.findOne(txn.getId()) == null)
			{

				com.ibm.api.cashew.beans.Transaction elasticTxn = new com.ibm.api.cashew.beans.Transaction();
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

				if (txn.getDetails() != null && txn.getDetails().getValue() != null)
				{
					if (!txn.getDetails().getType().equals(TransactionRequestType.TYPES.SELF.type()))
					{
						elasticTxn.setTxnType(com.ibm.api.cashew.beans.Transaction.TXN_TYPE_ROTATE);
					}
					else
					{
						if (txn.getDetails().getValue().getAmount() < 0)
						{
							// txn is a debit txn
							elasticTxn.setTxnType(com.ibm.api.cashew.beans.Transaction.TXN_TYPE_DEBIT);
						}
						else if (txn.getDetails().getValue().getAmount() > 0)
						{
							elasticTxn.setTxnType(com.ibm.api.cashew.beans.Transaction.TXN_TYPE_CREDIT);
						}
					}
				}

				User user = new User();
				user.setUserId(appUser);

				elasticTxn.setUserInfo(user);
				elasticTxnList.add(elasticTxn);

			}
		}

		return elasticTxnList;

	}

}