package com.ibm.big.oauth2server.db;

import java.util.List;

import com.ibm.big.oauth2server.beans.User;


public interface MongoUserRepositoryCustom
{
	public long updateUserLock(String userId, boolean locked);

	public List<User> findUsersFTS(String any, int fetchLimit);

	public long updatePassword(String userId, String newPwd);

	public long updatePhone(String userId, String phone);

	public long addExpertise(String userId, String expertise);

	public long addRole(String userId, String role);

}
