package com.ibm.api.cashew.services;

import java.util.Date;
import java.util.Set;

import com.ibm.api.cashew.beans.Tag;
import com.ibm.api.cashew.beans.User;

public interface UserService
{
	public User findUserById(String userId);

	public User createUser(User user);

	public User updateUser(User user);

	public long changePhone(String userId, String phoneNumber) throws Exception;

	public long changeEmail(String userId, String email);
	
	public long changeDOB(String userId, Date dob);

	public Set<Tag> getTags();

	long addTags(Set<Tag> tags, String userId);

}