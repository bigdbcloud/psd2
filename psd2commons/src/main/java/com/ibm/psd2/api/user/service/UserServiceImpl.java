package com.ibm.psd2.api.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.psd2.api.user.db.MongoUserInfoRepository;
import com.ibm.psd2.datamodel.user.UserInfo;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	MongoUserInfoRepository muir;

	@Override
	public UserInfo getUserDetails(String username)
	{
		return muir.findOne(username);
	}

}
