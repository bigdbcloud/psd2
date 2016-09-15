package com.ibm.psd2.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.ViewId;

public abstract class APIController
{
	private static final Logger logger = LogManager.getLogger(APIController.class);

	protected boolean validateSubscription(SubscriptionInfo s)
	{
		if (s != null && SubscriptionInfo.STATUS_ACTIVE.equals(s.getStatus()))
		{
			return true;
		}

		return false;
	}

	protected boolean validateSubscription(SubscriptionInfo s, ViewId view)
	{
		logger.info("Method Arguments = " + s);
		logger.info("Method Arguments = " + view);

		if (s != null && view != null && SubscriptionInfo.STATUS_ACTIVE.equals(s.getStatus()) && s.getViewIds() != null
				&& !s.getViewIds().isEmpty() && s.getViewIds().contains(view))
		{
			return true;
		}

		return false;
	}

}
