package com.ibm.psd2.api.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalSecurityConfig extends GlobalMethodSecurityConfiguration
{
	@Autowired
	SubscriptionEvaluator se;
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler()
	{
		OAuth2MethodSecurityExpressionHandler omseh = new OAuth2MethodSecurityExpressionHandler();
		omseh.setPermissionEvaluator(se);
		return omseh;
	}
}
