package com.ibm.psd2.api.security;

import java.io.Serializable;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import com.ibm.psd2.commons.datamodel.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.datamodel.subscription.ViewId;
import com.ibm.psd2.commons.subscription.service.SubscriptionService;

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
		// permission = owner, etc....
		String viewId = (String) permission;

		ViewId view = new ViewId();
		view.setId(viewId);

		OAuth2Authentication auth = (OAuth2Authentication) authentication;

		StringTokenizer st = new StringTokenizer(target, ".");

		if (st.hasMoreTokens())
		{
			bankId = st.nextToken();
		}
		if (st.hasMoreTokens())
		{
			accountId = st.nextToken();
		}

		logger.info("Method Arguments username: " + auth.getPrincipal() + " clientId: "
				+ auth.getOAuth2Request().getClientId() + " accountId: " + accountId + " bankId:" + bankId);
		logger.info("subscriptionService: " + subService);

		if (bankId != null)
		{
			String username = (String) auth.getPrincipal();
			String clientId = (String) auth.getOAuth2Request().getClientId();

			if (accountId != null)
			{
				SubscriptionInfo si = subService.getSubscriptionInfo(username, clientId, accountId, bankId);
				return validateSubscription(si, view);
			}
			else
			{
				List<SubscriptionInfo> sis = subService.getSubscriptionInfo(username, clientId, bankId);
				if (sis == null)
				{
					return false;
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
