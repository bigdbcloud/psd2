package com.ibm.big.oauth2server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.big.oauth2server.services.UserService;

@Component
public class SocialLoginAuthSuccessHandlerFactory
{
	@Autowired
	UserDetailsServiceImpl uds;

	@Autowired
	UserService userService;

	public FacebookAuthSuccessHandler getAuthenticationSuccessHandler(String provider)
	{
		if ("facebook".equals(provider))
		{
			FacebookAuthSuccessHandler slash = new FacebookAuthSuccessHandler();
			slash.setUserDetailsService(uds);
			slash.setUserService(userService);
			return slash;
		}
		else
		{
			throw new UnsupportedOperationException("Other providers are not yet supported");
		}
	}

}
