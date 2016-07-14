package com.ibm.psd2.api.common.oauth2;

import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

public class GlobalSecurityConfig //extends GlobalMethodSecurityConfiguration 
{
//	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler()
	{
		return new OAuth2MethodSecurityExpressionHandler();
	}
}
