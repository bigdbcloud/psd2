package com.ibm.big.oauth2server.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ibm.big.oauth2server.beans.User;
import com.ibm.big.oauth2server.services.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
	private final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		logger.debug("loading user details for username = " + username);
		User user = userService.findUserById(username);

		if (user == null)
		{
			throw new UsernameNotFoundException("User not found for username = " + username);
		}
		if (user.isLocked())
		{
			throw new UsernameNotFoundException("User " + username + " is locked");

		}

		List<String> roles = user.getRoles();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (roles != null)
		{
			for (Iterator<String> iterator = roles.iterator(); iterator.hasNext();)
			{
				String role = iterator.next();
				authorities.add(new SimpleGrantedAuthority(role));
			}
		}

		// if (user.isSocial())
		// {
		// logger.info("Setting password to N/A since its a social login");
		// user.setPwd("N/A");
		// }

		return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPwd(), authorities);
	}

}
