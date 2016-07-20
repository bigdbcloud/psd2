package com.ibm.psd2.api.subscriptionmgmt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.subscription.dao.SubscriptionDao;
import com.ibm.psd2.api.subscription.dao.SubscriptionRequestDao;
import com.ibm.psd2.commons.beans.ChallengeAnswerBean;
import com.ibm.psd2.commons.beans.SimpleResponseBean;
import com.ibm.psd2.commons.beans.subscription.SubscriptionRequestBean;

@RestController
public class SubscriptionManagementController
{
	private static final Logger logger = LogManager.getLogger(SubscriptionManagementController.class);

	@Autowired
	SubscriptionDao sdao;

	@Autowired
	SubscriptionRequestDao srdao;
	
	@Autowired
	SubscriptionRules srules;

	@Value("${version}")
	private String version;

	@RequestMapping(method = RequestMethod.POST, value = "/admin/subscription/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SimpleResponseBean> createSubscription(@PathVariable("id") String id,
			@RequestBody(required = true) ChallengeAnswerBean cab)
	{
		ResponseEntity<SimpleResponseBean> response;
		SimpleResponseBean srb = new SimpleResponseBean();
		try
		{
			SubscriptionRequestBean sr = srdao.getSubscriptionRequestByIdAndChallenge(id, cab);
			if (sr == null)
			{
				throw new IllegalArgumentException("Subscription Request Not Found for id = " + id + " , challenge.id = " + cab.getId());
			}
			
			if (!srules.validateTxnChallengeAnswer(cab))
			{
				throw new IllegalArgumentException("Challenge Answer is not correct");
			}
			
			sdao.createSubscriptionInfo(sr.getSubscriptionInfo());
			srdao.updateSubscriptionRequestStatus(id, SubscriptionRequestBean.STATUS_SUBSCRIBED);
			srb.setResponseCode(SimpleResponseBean.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
			
		} catch (Exception e)
		{
			logger.error(e);
			srb.setResponseCode(SimpleResponseBean.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

}
