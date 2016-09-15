package com.ibm.psd2.api.aip.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.db.MongoBankAccountDetailsRepository;
import com.ibm.psd2.datamodel.aip.BankAccountDetails;

@Service
public class BankAccountDetailsServiceImpl implements BankAccountDetailsService
{

	private final Logger logger = LogManager.getLogger(BankAccountDetailsServiceImpl.class);

	// @Autowired
	// private MongoConnection conn;

	// @Autowired
	// private MongoDocumentParser mdp;

	@Value("${mongodb.collection.bankaccounts}")
	private String bankAccounts;

	@Autowired
	MongoBankAccountDetailsRepository mgadr;

	// private static final String LOG_PREFIX = "document = ";

	@Override
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId)
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);
		return mgadr.findByIdAndBankId(accountId, bankId);

	}

	@Override
	public List<BankAccountDetails> getBankAccounts(String username, String bank_id)
	{
		logger.info("bankId = " + bank_id + ", username = " + username);
		return mgadr.findByUsernameAndBankId(username, bank_id);
	}

	@Override
	public BankAccountDetails getBankAccountDetails(String bankId, String accountId, String username)
	{
		logger.info("bankId = " + bankId + ", accountId = " + accountId);

		return mgadr.findByIdAndBankIdAndUsername(accountId, bankId, username);

	}

	@Override
	public void createBankAccountDetails(BankAccountDetails b)
	{
		mgadr.save(b);
	}

	@Override
	public void updateBalance(String bankId, String accountId, double balance)
	{
		mgadr.updateBalance(bankId, accountId, balance);
	}
	
	

}
