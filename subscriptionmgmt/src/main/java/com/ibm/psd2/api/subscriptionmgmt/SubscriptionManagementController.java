package com.ibm.psd2.api.subscriptionmgmt;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.subscription.dao.SubscriptionDao;
import com.ibm.psd2.api.subscription.dao.SubscriptionRequestDao;
import com.ibm.psd2.commons.beans.ChallengeAnswer;
import com.ibm.psd2.commons.beans.SimpleResponse;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.beans.subscription.SubscriptionRequest;

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
	public @ResponseBody ResponseEntity<SimpleResponse> createSubscription(@PathVariable("id") String id,
			@RequestBody(required = true) ChallengeAnswer cab)
	{
		ResponseEntity<SimpleResponse> response;
		SimpleResponse srb = new SimpleResponse();
		try
		{
			SubscriptionRequest sr = srdao.getSubscriptionRequestByIdAndChallenge(id, cab);
			if (sr == null)
			{
				throw new IllegalArgumentException("Subscription Request Not Found for id = " + id + " , challenge.id = " + cab.getId());
			}
			
			if (!srules.validateTxnChallengeAnswer(cab))
			{
				throw new IllegalArgumentException("Challenge Answer is not correct");
			}
			
			sdao.createSubscriptionInfo(sr.getSubscriptionInfo());
			srdao.updateSubscriptionRequestStatus(id, SubscriptionRequest.STATUS_SUBSCRIBED);
			srb.setResponseCode(SimpleResponse.CODE_SUCCESS);
			response = ResponseEntity.ok(srb);
			
		} catch (Exception e)
		{
			logger.error(e);
			srb.setResponseCode(SimpleResponse.CODE_ERROR);
			srb.setResponseMessage(e.getMessage());
			response = ResponseEntity.badRequest().body(srb);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/subscription/request", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<SubscriptionRequest> createSubscription(
			@RequestBody(required=true) SubscriptionRequest subscriptionRequest)
	{
		ResponseEntity<SubscriptionRequest> response;
		try
		{
			SubscriptionRequest sreturn = srdao.createSubscriptionRequest(subscriptionRequest);
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<SubscriptionInfo>> getSubscriptionInfo(@RequestHeader(value = "user", required = true) String user,
			@RequestHeader(value = "client", required = true) String client)
	{
		ResponseEntity<List<SubscriptionInfo>> response;
		try
		{
			List<SubscriptionInfo> sreturn = sdao.getSubscriptionInfo(user, client);
			response = ResponseEntity.ok(sreturn);
		} catch (Exception e)
		{
			logger.error(e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}
}
