package com.ibm.api.cashew.services;

import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.db.MongoUserRepository;

@Service
public class UserServiceImpl implements UserService
{
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	MongoUserRepository userRepo;

	@Value("${user.role.lowest}")
	private String ROLE_USER;

	private User getUserById(String userId)
	{
		logger.debug("finding user with userId: " + userId);
		return userRepo.findOne(userId);
	}

	@Override
	public User findUserById(String userId)
	{
		User user = getUserById(userId);
		return user;
	}

	@Override
	public User findUserByEmail(String email)
	{
		return userRepo.findByEmail(email);
	}

	@Override
	public User createUser(User user)
	{
		logger.debug("Creating user = " + user);

		if (user == null)
		{
			throw new IllegalArgumentException("User can't be null");
		}

		if (user.getUserId() == null)
		{
			throw new IllegalArgumentException("userId can't be null");
		}

		if (user.getAuthProvider() == null || user.getAuthProviderClientId() == null)
		{
			throw new IllegalArgumentException("Client Details are not set");
		}

		if (getUserById(user.getUserId()) != null)
		{
			throw new IllegalArgumentException("User already exists");
		}

		user.setLocked(false);

		user = userRepo.save(user);
		return user;
	}

	@Override
	public User updateUser(User user)
	{
		logger.debug("Updating user = " + user);

		if (user == null)
		{
			throw new IllegalArgumentException("User can't be null");
		}

		User existingUser = getUserById(user.getUserId());
		if (existingUser == null)
		{
			throw new IllegalArgumentException("User doesn't exist");
		}

		if (user.getName() != null && !user.getName().isEmpty())
		{
			existingUser.setName(user.getName());
		}

		if (user.getMobileNumber() != null && !user.getMobileNumber().isEmpty())
		{
			existingUser.setMobileNumber(user.getMobileNumber());
		}
		
		if (user.getEmail() != null && !user.getEmail().isEmpty())
		{
			existingUser.setEmail(user.getEmail());
		}
		
		logger.debug("User will be updated as: " + existingUser);

		return userRepo.save(existingUser);
	}

	@Override
	public long addExpertise(String userId, String expertise)
	{
		logger.info("updating user's expertise: " + userId + " : " + expertise);

		if (expertise == null || expertise.isEmpty())
		{
			throw new IllegalArgumentException("Expertise can't be null");
		}
		return userRepo.addExpertise(userId, expertise);
	}

	@Override
	public long changePhone(String userId, String phoneNumber)
	{
		logger.info("changing user's phonenumber: " + userId + " : " + phoneNumber);
		if (phoneNumber == null || phoneNumber.isEmpty())
		{
			throw new IllegalArgumentException("PhoneNumber can't be null");
		}
		return userRepo.updatePhone(userId, phoneNumber);
	}

}
