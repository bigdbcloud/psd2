package com.ibm.psd2.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalSecurityConfig extends GlobalMethodSecurityConfiguration
{
	@Autowired
	SubscriptionEvaluator se;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler()
	{
		DefaultMethodSecurityExpressionHandler  omseh = new DefaultMethodSecurityExpressionHandler();
		omseh.setPermissionEvaluator(se);
		return omseh;
	}
}
