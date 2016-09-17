package com.ibm.psd2.oauth2server.services;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.psd2.oauth2server.beans.UserInfo;
import com.ibm.psd2.oauth2server.db.MongoUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
	private final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private MongoUserRepository mur;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		
		UserInfo userInfo = null;
		
		try
		{
			userInfo = getUserInfo(username);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		
		GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
		UserDetails userDetails = (UserDetails)new User(userInfo.getUsername(), userInfo.getPassword(), Arrays.asList(authority));

		return userDetails;
	}

	private UserInfo getUserInfo(String username) throws Exception
	{
		return mur.findOne(username);
	}
	
	public void createUserInfo(UserInfo u) throws Exception
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if (mur.findOne(u.getUsername()) != null)
		{
			throw new IllegalArgumentException("User already exists: " + u.getUsername());
		}
		
		u.setPassword(encoder.encode(u.getPassword()));
		
		mur.save(u);
	}

}
