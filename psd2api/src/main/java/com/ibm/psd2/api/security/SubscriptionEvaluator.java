package com.ibm.psd2.api.security;

import java.io.Serializable;
import java.util.Iterator;
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
import com.ibm.psd2.datamodel.subscription.TransactionRequestType;
import com.ibm.psd2.datamodel.subscription.ViewId;

@Component
public class SubscriptionEvaluator implements PermissionEvaluator
{
	private final Logger logger = LogManager.getLogger(SubscriptionEvaluator.class);

	@Autowired
	SubscriptionService subService;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission)
	{
		String target = (String) targetDomainObject;
		PermissionData ps = new PermissionData(target, authentication.getName(), (String) permission, subService);
		return ps.validate();
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission)
	{
		return false;
	}

}

class PermissionData
{
	private final Logger logger = LogManager.getLogger(PermissionData.class);

	String userId;
	String viewId;
	String accountId;
	String bankId;
	String clientId;
	String permission;
	String txnType;
	SubscriptionService subsService;

	PermissionData(String target, String clientId, String permission, SubscriptionService service)
	{
		StringTokenizer st = new StringTokenizer(target, ".");

		if (st.hasMoreTokens())
		{
			userId = st.nextToken();
		}

		if (st.hasMoreTokens())
		{
			bankId = st.nextToken();
		}
		if (st.hasMoreTokens())
		{
			accountId = st.nextToken();
		}
		if (st.hasMoreTokens())
		{
			viewId = st.nextToken();
		}
		if (st.hasMoreTokens())
		{
			txnType = st.nextToken();
		}

		this.subsService = service;

		this.clientId = clientId;
		this.permission = permission;

		logger.debug("Method Arguments username: " + userId + " clientId: " + clientId + " accountId: " + accountId
				+ " bankId:" + bankId + ", viewId: " + viewId + ", txnType: " + txnType + ", permission: " + permission);
	}

	public String getUserId()
	{
		return userId;
	}

	public String getViewId()
	{
		return viewId;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public String getBankId()
	{
		return bankId;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public String getClientId()
	{
		return clientId;
	}

	public String getPermission()
	{
		return permission;
	}

	public boolean validate()
	{
		if ("getAllAccounts".equals(permission))
		{
			return validateGetAllAccounts();
		}
		else if ("getAccountInfo".equals(permission))
		{
			return validateGetAccountInfo();
		}
		else if ("createTransactionRequest".equals(permission))
		{
			return validateCreateTransactionRequest();
		}
		else if("getTransactionRequestTypes".equals(permission))
		{
			return validateGetTransactionRequestTypes();
		}

		return false;

	}

	private boolean validateGetAllAccounts()
	{
		List<SubscriptionInfo> sis = subsService.getSubscriptionInfo(userId, clientId, bankId);
		if (sis != null && !sis.isEmpty())
		{
			return true;
		}
		return false;
	}

	private boolean validateGetAccountInfo()
	{
		ViewId view = new ViewId();
		view.setId(viewId);
		SubscriptionInfo si = subsService.getSubscriptionInfo(userId, clientId, accountId, bankId);
		return validateSubscription(si, view);
	}

	private boolean validateCreateTransactionRequest()
	{
		ViewId view = new ViewId();
		view.setId(viewId);

		SubscriptionInfo si = subsService.getSubscriptionInfo(userId, clientId, accountId, bankId);

		if (validateSubscription(si, view))
		{
			if (si.getTransactionRequestTypes() != null && !si.getTransactionRequestTypes().isEmpty())
			{
				for (Iterator<TransactionRequestType> iterator = si.getTransactionRequestTypes().iterator(); iterator
						.hasNext();)
				{
					TransactionRequestType type = iterator.next();
					if (type.getValue().equals(txnType))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean validateGetTransactionRequestTypes()
	{
		ViewId view = new ViewId();
		view.setId(viewId);

		SubscriptionInfo si = subsService.getSubscriptionInfo(userId, clientId, accountId, bankId);

		if (validateSubscription(si, view))
		{
			if (si.getTransactionRequestTypes() != null && !si.getTransactionRequestTypes().isEmpty())
			{
				return true;
			}
		}
		return false;
	}

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

}
