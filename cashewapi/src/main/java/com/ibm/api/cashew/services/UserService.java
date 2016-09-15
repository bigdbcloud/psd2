package com.ibm.api.cashew.services;

import com.ibm.api.cashew.beans.User;

public interface UserService
{
	public User findUserById(String userId);

	public User findUserByEmail(String email);

	public User createUser(User user);

	public User updateUser(User user);

	public long addExpertise(String userId, String expertise);

	public long changePhone(String userId, String phoneNumber) throws Exception;

}