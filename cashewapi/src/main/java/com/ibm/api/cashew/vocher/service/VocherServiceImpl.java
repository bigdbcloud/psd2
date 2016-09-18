package com.ibm.api.cashew.vocher.service;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ibm.api.cashew.bank.service.BankService;
import com.ibm.api.cashew.beans.TxnDetails;
import com.ibm.api.cashew.beans.Vocher;
import com.ibm.api.cashew.utils.Utils;
import com.ibm.api.cashew.vocher.db.MongoVocherRepository;
import com.ibm.psd2.datamodel.pisp.TxnParty;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;

@Service
public class VocherServiceImpl implements VocherService {

	@Autowired
	private MongoVocherRepository mongoVocherRepos;

	@Autowired
	private BankService bankService;

	@Autowired
	private Utils utils;

	@Value("${cashew.psd2.central.account}")
	private String centralAcct;

	@Value("${cashew.psd2.central.bank}")
	private String centralBank;

	@Value("${cashew.psd2.central.account.user}")
	private String centrlAcctUser;

	@Override
	public Vocher createVocher(Vocher vocher) {

		validateCreateVocher(vocher);

		TxnParty txnTo = new TxnParty(centralBank, centralAcct);
		for (TxnDetails txnDetail : vocher.getAcctFrom()) {

			TxnRequest txnRequest = new TxnRequest();
			txnRequest.setTo(txnTo);
			txnRequest.setValue(txnDetail.getValue());
			txnRequest.setDescription(vocher.getDescription());

			TxnParty txnFrom = new TxnParty(txnDetail.getBankId(), txnDetail.getAccountId());

			bankService.createTransaction(txnRequest, txnFrom, TransactionRequestType.TYPES.INTER_BANK.name(),
					txnDetail.getCustomerId());
		}

		vocher.setCode(utils.getVocherCode());
		vocher.setCreationDate(Vocher.DATE_FORMAT.format(new Date()));
		vocher.setExpiryDate(utils.getVocherExpDate());

		return mongoVocherRepos.save(vocher);

	}

	@Override
	public Vocher redeemVocher(Vocher vocher) {

		if (StringUtils.isBlank(vocher.getCode())) {
			throw new IllegalArgumentException("Vocher code is required");
		}

		Vocher existngVocher = mongoVocherRepos.findOne(vocher.getCode());
		validateRedeemVocher(vocher, existngVocher);

		double amtRedeemed = getAmountRedeemed(vocher, existngVocher);
		double amtLeft = vocher.getAmount().getAmount() - amtRedeemed;

		TxnParty txnFrom = new TxnParty(centralBank, centralAcct);
		for (TxnDetails txnReq : vocher.getRedeemedTo()) {

			TxnParty txnTo = new TxnParty(txnReq.getBankId(), txnReq.getAccountId());
			TxnRequest txnRequest = new TxnRequest();
			txnRequest.setTo(txnTo);
			txnRequest.setValue(txnReq.getValue());
			txnRequest.setDescription(vocher.getDescription());

			bankService.createTransaction(txnRequest, txnFrom, TransactionRequestType.TYPES.INTER_BANK.name(),
					centrlAcctUser);
			amtLeft = amtLeft - txnReq.getValue().getAmount();

			if (amtLeft == 0) {
				vocher.setRedeemed(true);
			}

			mongoVocherRepos.updateVocher(vocher, txnReq);

		}

		return mongoVocherRepos.findOne(vocher.getCode());
	}

	private void validateCreateVocher(Vocher vocher) {

		if (vocher == null) {
			throw new IllegalArgumentException("Invalid Vocher request");
		}

		if (vocher.getAcctFrom() == null || CollectionUtils.isEmpty(vocher.getAcctFrom())) {
			throw new IllegalArgumentException("Account details is required to create Vocher");
		}

		if (vocher.getAmount() == null || vocher.getAmount().getAmount() == 0
				|| StringUtils.isBlank(vocher.getAmount().getCurrency())) {
			throw new IllegalArgumentException("Vocher amount should be specified");
		}

		double totalAmt = 0.00;

		for (TxnDetails txDetails : vocher.getAcctFrom()) {

			if (txDetails.getValue().getCurrency() == null
					|| !txDetails.getValue().getCurrency().equals(vocher.getAmount().getCurrency())) {
				throw new IllegalArgumentException("Invalid currency specified for vocher amount");
			}

			if (txDetails != null && txDetails.getValue().getAmount() != 0) {
				totalAmt = totalAmt + txDetails.getValue().getAmount();
			}
		}

		if (totalAmt > vocher.getAmount().getAmount() || totalAmt < vocher.getAmount().getAmount()) {

			throw new IllegalArgumentException("Amount distribution for vocher creation is incorrect");
		}

	}

	private void validateRedeemVocher(Vocher vocher, Vocher existingVocher) {

		if (CollectionUtils.isEmpty(vocher.getRedeemedTo())) {

			throw new IllegalArgumentException("Account details required to redeem vocher");
		}

		if (existingVocher == null) {
			throw new IllegalArgumentException("Invalid vocher code");
		}

		if (existingVocher.isRedeemed()) {
			throw new IllegalArgumentException("Vocher amount is already redeemed");
		}

		Date expiryDate;

		try {
			expiryDate = Vocher.DATE_FORMAT.parse(existingVocher.getExpiryDate());
		} catch (ParseException e) {

			throw new IllegalArgumentException("Invalid vocher expiry date");
		}

		if (expiryDate.before(new Date())) {
			throw new IllegalArgumentException("Vocher has expired");
		}

		double amtLeft = 0.00;
		double amtRedeemed = getAmountRedeemed(vocher, existingVocher);
		double amtToRedeem = getAmountToRedeem(vocher, existingVocher);

		amtLeft = vocher.getAmount().getAmount() - amtRedeemed;

		for (TxnDetails txDetails : vocher.getRedeemedTo()) {
			if (txDetails.getValue().getCurrency() == null
					|| !txDetails.getValue().getCurrency().equals(vocher.getAmount().getCurrency())) {

				throw new IllegalArgumentException("Invalid currency specified for redeem amount");
			}
		}

		if (amtToRedeem > amtLeft) {
			throw new IllegalArgumentException("Amount to be redeemed is greater than vocher balance");
		}

	}

	private double getAmountToRedeem(Vocher vocher, Vocher existingVocher) {

		double amtToRedeem = 0.00;

		for (TxnDetails txDetails : vocher.getRedeemedTo()) {

			if (txDetails != null && txDetails.getValue().getAmount() != 0) {
				amtToRedeem = amtToRedeem + txDetails.getValue().getAmount();
			}
		}

		return amtToRedeem;
	}

	private double getAmountRedeemed(Vocher vocher, Vocher existingVocher) {

		double amtRedeemed = 0.00;
		if (!CollectionUtils.isEmpty(existingVocher.getRedeemedTo())) {

			for (TxnDetails txDetails : existingVocher.getRedeemedTo()) {
				if (txDetails != null && txDetails.getValue().getAmount() != 0) {
					amtRedeemed = amtRedeemed + txDetails.getValue().getAmount();
				}
			}
		}

		return amtRedeemed;
	}

}
