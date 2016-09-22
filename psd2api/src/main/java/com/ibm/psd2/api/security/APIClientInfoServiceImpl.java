package com.ibm.psd2.api.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class APIClientInfoServiceImpl implements UserDetailsService
{
	private final Logger logger = LogManager.getLogger(APIClientInfoServiceImpl.class);

	@Autowired
	MongoAPIClientRepository macr;

	@Override
	public UserDetails loadUserByUsername(String clientname) throws UsernameNotFoundException
	{
		logger.debug("loading client: " + clientname);
		APIClient ac = macr.findOne(clientname);

		if (ac == null)
		{
			throw new UsernameNotFoundException("User not found for username = " + clientname);
		}
		if (ac.isLocked())
		{
			throw new UsernameNotFoundException("Client " + clientname + " is locked");

		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		return new User(ac.getClientname(), ac.getPassword(), authorities);
	}

}
