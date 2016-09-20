package com.ibm.psd2.api.subscription.service;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.aip.services.BankAccountDetailsService;
import com.ibm.psd2.api.subscription.db.MongoSubscriptionRequestRepository;
import com.ibm.psd2.api.twilio.message.service.MessageService;
import com.ibm.psd2.api.user.service.UserService;
import com.ibm.psd2.datamodel.Challenge;
import com.ibm.psd2.datamodel.ChallengeAnswer;
import com.ibm.psd2.datamodel.aip.BankAccountDetails;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.SubscriptionRequest;
import com.ibm.psd2.datamodel.user.UserInfo;
import com.ibm.psd2.utils.UUIDGenerator;
import com.twilio.sdk.TwilioRestException;

@Service
public class SubscriptionRequestServiceImpl implements SubscriptionRequestService {
	private final Logger logger = LogManager.getLogger(SubscriptionRequestServiceImpl.class);

	private static final int CHALLENGE_MAX_ATTEMPTS = 3;

	@Autowired
	private MongoSubscriptionRequestRepository msrr;

	@Autowired
	private SubscriptionService subsService;

	@Autowired
	private SubscriptionRules srules;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private BankAccountDetailsService bankAcctService;

	@Value("${bank.subscription.response.number}")
	private String bankSubscribeRspnsNo;

	@Override
	public SubscriptionRequest getSubscriptionRequestByIdAndChallenge(String id, ChallengeAnswer cab) {
		logger.info("id = " + id);

		if (cab == null) {
			throw new IllegalArgumentException("Invalid ChallengeAnswer. It can't be null");
		}

		SubscriptionRequest sr = msrr.findByIdAndChallengeId(id, cab.getId());
		if (sr != null) {
			sr.getChallenge().setAnswer(null);
		}
		return sr;
	}

	@Override
	public SubscriptionRequest createSubscriptionRequest(SubscriptionRequest s) {
		logger.info("Subscription Request = " + s);

		s.setId(UUIDGenerator.generateUUID());
		s.setCreationDate(new Date());
		s.setStatus(SubscriptionRequest.STATUS_INITIATED);

		Challenge c = new Challenge();
		c.setId(UUIDGenerator.generateUUID());
		c.setChallengeType(Challenge.ACCOUNT_SUBSCRIPTION);
		c.setAllowedAttempts(CHALLENGE_MAX_ATTEMPTS);
		c.setAnswer(UUIDGenerator.randomAlphaNumeric(10));
		s.setChallenge(c);

		SubscriptionRequest sr = msrr.save(s);

		// send subscription challenge answer to user registered Mobile No

		String userMobNo = null;

		BankAccountDetails bankAcct = bankAcctService.getBankAccountDetails(s.getSubscriptionInfo().getBankId(),
				s.getSubscriptionInfo().getAccountId());
		if (bankAcct != null && StringUtils.isNotBlank(bankAcct.getUsername())) {
			UserInfo userInfo = userService.getUserDetails(bankAcct.getUsername());

			if (userInfo != null) {
				userMobNo = userInfo.getMobileNumber();
			}
		}

		String msg = MessageFormat.format(Challenge.CHALLENGE_RESPONSE, c.getAnswer());

		logger.debug("Sending challenge answer to user mobile no: {} ");

		if (StringUtils.isNotBlank(userMobNo)) {

			try {
				messageService.sendMessage(bankSubscribeRspnsNo, userMobNo, msg);
			} catch (TwilioRestException e) {
				logger.error("Failed to send challenge answer for subscription request : {}", s.getId());
			}
		}

		// Don't let the answer go out
		sr.getChallenge().setAnswer(null);
		return sr;
	}

	@Override
	public SubscriptionInfo validateTxnChallengeAnswer(String id, ChallengeAnswer cab) {
		SubscriptionRequest sr = msrr.findOne(id);

		if (sr == null) {
			throw new IllegalArgumentException("subscription request id is not valid");
		}
		if (cab == null) {
			throw new IllegalArgumentException("Challenge Answer can't be null");
		}
		if (!sr.getChallenge().getId().equals(cab.getId())) {
			throw new IllegalArgumentException("Challenge is not valid");
		}
		if (!sr.getChallenge().getAnswer().equals(cab.getAnswer())) {
			throw new IllegalArgumentException("Challenge Answer is not correct");
		}

		SubscriptionInfo si = subsService.createSubscriptionInfo(sr.getSubscriptionInfo());
		updateSubscriptionRequestStatus(id, SubscriptionRequest.STATUS_SUBSCRIBED);
		return si;

	}

	@Override
	public int updateSubscriptionRequestStatus(String id, String status) {
		return msrr.updateStatus(id, status);
	}

}
