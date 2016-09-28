package com.ibm.big.oauth2server.services;

import java.util.List;

import com.ibm.big.oauth2server.beans.User;

public interface UserService
{

	public User findUserById(String userId);

	public User findUserByEmail(String email);

	public List<User> findUsersByFreeText(String anyField);

	public User createUser(User user);

	public User updateUser(User user);

	public long changePassword(String userId, String newPwd);

	public long addExpertise(String userId, String expertise);

	public long addRole(String userId, String role) throws Exception;

	public long changePhone(String userId, String phoneNumber) throws Exception;

}