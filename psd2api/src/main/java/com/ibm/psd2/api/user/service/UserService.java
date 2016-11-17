package com.ibm.psd2.api.user.service;

import com.ibm.psd2.datamodel.user.UserInfo;

public interface UserService
{
	public UserInfo getUserDetails(String username);
}
