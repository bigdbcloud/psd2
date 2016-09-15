package com.ibm.big.oauth2server.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.big.oauth2server.beans.User;
import com.ibm.big.oauth2server.db.MongoUserRepository;

@Service
public class UserServiceImpl implements UserService
{
	private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	MongoUserRepository userRepo;

	@Value("${mongodbCollection.users.fetchLimit}")
	int fetchLimit;

	@Value("${user.role.lowest}")
	private String ROLE_USER;

	@Value("${users.roles}")
	private String roles;

	@Value("${bigoauth2server.provider.name}")
	private String providerName;

	private HashSet<String> roleSet;

	private HashSet<String> getRoles()
	{
		if (roleSet == null)
		{
			StringTokenizer st = new StringTokenizer(roles, ",");
			roleSet = new HashSet<>();
			while (st.hasMoreTokens())
			{
				String role = st.nextToken();
				logger.info("valid role = " + role);
				roleSet.add(role);
			}
		}
		return roleSet;
	}

	private boolean validateEmail(String email)
	{
		boolean b = false;
		EmailValidator ev = EmailValidator.getInstance();
		b = ev.isValid(email);
		return b;
	}

	private boolean validateRole(String role)
	{
		if (role == null || role.isEmpty())
		{
			return false;
		}
		return getRoles().contains(role);
	}

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
		User user = userRepo.findByEmail(email);
		if (user != null)
		{
			user.setPwd(null);
		}
		return user;
	}

	@Override
	public List<User> findUsersByFreeText(String anyField)
	{
		return userRepo.findUsersFTS(anyField, fetchLimit);
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

		if (user.getEmail() == null || !validateEmail(user.getEmail()))
		{
			throw new IllegalArgumentException("Email Address of the user is not valid: " + user.getEmail());
		}

		if (user.getAuthProvider() == null || user.getAuthProviderClientId() == null)
		{
			throw new IllegalArgumentException("Client Details are not set");
		}

		if (user.getPwd() == null || user.getPwd().isEmpty())
		{
			throw new IllegalArgumentException("Password can't be null or empty");
		}
		
		if (user.getAuthProviderClientId() == null || user.getAuthProviderClientId().isEmpty())
		{
			throw new IllegalArgumentException("Client Id can't be null");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPwd(encoder.encode(user.getPwd()));
		user.setAuthProvider(providerName);

		if (getUserById(user.getUserId()) != null)
		{
			throw new IllegalArgumentException("User already exists");
		}

		ArrayList<String> roles = new ArrayList<>();
		roles.add(ROLE_USER);
		user.setRoles(roles);
		user.setAoe(null);

		user.setLocked(false);

		user = userRepo.save(user);
		user.setPwd(null);
		return user;
	}

	@Override
	public User updateUser(User user)
	{
		logger.debug("Creating user = " + user);

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

		return userRepo.save(existingUser);
	}

	@Override
	public long changePassword(String userId, String newPwd)
	{
		logger.info("changing pwd for user: " + userId);

		User user = getUserById(userId);

		if (newPwd == null || newPwd.isEmpty())
		{
			throw new IllegalArgumentException("New Password can't be null");
		}

		if (user == null)
		{
			throw new IllegalArgumentException("User Doesn't Exist");
		}

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return userRepo.updatePassword(userId, encoder.encode(newPwd));
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
	public long addRole(String userId, String role) throws Exception
	{
		logger.info("adding user's role: " + userId + " : " + role);
		if (!validateRole(role))
		{
			throw new IllegalArgumentException("Role can't be null");
		}

		return userRepo.addRole(userId, role);
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
