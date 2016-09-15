package com.ibm.psd2.api.security;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ibm.psd2.api.subscription.service.SubscriptionService;
import com.ibm.psd2.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.datamodel.subscription.ViewId;

@Component
public class SubscriptionEvaluator implements PermissionEvaluator
{
	private final Logger logger = LogManager.getLogger(SubscriptionEvaluator.class);

	@Autowired
	SubscriptionService subService;

	private boolean validateSubscription(SubscriptionInfo s)
	{
		if (s != null && SubscriptionInfo.STATUS_ACTIVE.equals(s.getStatus()))
		{
			return true;
		}

		return false;
	}

	private boolean validateSubscription(SubscriptionInfo s, ViewId view)
	{
		logger.info("Method Arguments SubscriptionInfo = " + s + " ViewId view: " + view);
		logger.info("Method Arguments = " + view);

		if (validateSubscription(s) && view != null && s.getViewIds() != null && !s.getViewIds().isEmpty()
				&& s.getViewIds().contains(view))
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission)
	{
		// bankId.accountId format
		String target = (String) targetDomainObject;
		String bankId = null;
		String accountId = null;
		String user = null;
		// permission = owner, etc....
		String viewId = (String) permission;

		ViewId view = new ViewId();
		view.setId(viewId);

		StringTokenizer st = new StringTokenizer(target, ".");
		
		if (st.hasMoreTokens())
		{
			user = st.nextToken();
		}

		if (st.hasMoreTokens())
		{
			bankId = st.nextToken();
		}
		if (st.hasMoreTokens())
		{
			accountId = st.nextToken();
		}

		logger.info("Method Arguments username: " + user + " clientId: "
				+ authentication.getPrincipal()  + " accountId: " + accountId + " bankId:" + bankId);
		logger.info("subscriptionService: " + subService);

		if (user != null && bankId != null)
		{
			String clientId = authentication.getName();

			if (accountId != null)
			{
				SubscriptionInfo si = subService.getSubscriptionInfo(user, clientId, accountId, bankId);
				return validateSubscription(si, view);
			}
			else
			{
				List<SubscriptionInfo> sis = subService.getSubscriptionInfo(user, clientId, bankId);
				if (sis != null && !sis.isEmpty())
				{
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission)
	{
		return false;
	}

}
