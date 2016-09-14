package com.ibm.big.oauth2server.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.ibm.big.oauth2server.beans.User;
import com.ibm.big.oauth2server.services.UserService;

public class FacebookAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private Logger logger = LogManager.getLogger(FacebookAuthSuccessHandler.class);

	UserDetailsService userDetailsService;

	UserService userService;

	public void setUserDetailsService(UserDetailsServiceImpl uds)
	{
		this.userDetailsService = uds;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException
	{
		logger.debug("Authentication Success Intercepted to locally store data");

		if (authentication instanceof OAuth2Authentication)
		{
			OAuth2Authentication oa = (OAuth2Authentication) authentication;
			logger.debug("user authentication class is of type: " + oa.getUserAuthentication().getClass().getName());
			logger.debug("user authentication details is : " + oa.getUserAuthentication().getDetails());
			if (oa.getUserAuthentication().getDetails() != null)
			{
				logger.debug("user authentication details is : "
						+ oa.getUserAuthentication().getDetails().getClass().getName());
			}
			String userId = oa.getName();
			UserDetails ud;
			try
			{
				ud = userDetailsService.loadUserByUsername(userId);
				/**
				 * This means we have found the user in our local repo
				 */
				authentication = buildNewAuth(oa, ud);
			} catch (UsernameNotFoundException e)
			{
				/**
				 * create user in the user repo
				 */

				User user = new User();
				user.setUserId(userId);
				user.setLocked(false);
				user.setAuthProvider("facebook");
				if (oa.getOAuth2Request() != null)
				{
					user.setAuthProviderClientId(oa.getOAuth2Request().getClientId());
				}

				if (oa.getUserAuthentication().getDetails() != null
						&& oa.getUserAuthentication().getDetails() instanceof Map)
				{
					Map<String, String> details = (Map<String, String>) oa.getUserAuthentication().getDetails();
					user.setName(details.get("name"));
				}
				
//				user.setSocial(true);
				userService.createUser(user);
				
				ud = userDetailsService.loadUserByUsername(userId);
				authentication = buildNewAuth(oa, ud);
			}
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

	private OAuth2Authentication buildNewAuth(OAuth2Authentication oa, UserDetails ud)
	{
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(ud,
				oa.getUserAuthentication().getCredentials());
//		upat.setAuthenticated(true);
		upat.setDetails(oa.getUserAuthentication().getDetails());
		OAuth2Request or = oa.getOAuth2Request();
		Object details = oa.getDetails();
		oa = new OAuth2Authentication(or, upat);
		oa.setAuthenticated(true);
		oa.setDetails(details);
		return oa;
	}
}
