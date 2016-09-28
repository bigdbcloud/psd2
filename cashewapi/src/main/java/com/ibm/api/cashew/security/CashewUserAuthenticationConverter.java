package com.ibm.api.cashew.security;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import com.ibm.api.cashew.beans.User;
import com.ibm.api.cashew.services.UserService;

/**
 * Default implementation of {@link UserAuthenticationConverter}. Converts to
 * and from an Authentication using only its name and authorities.
 * 
 * @author Dave Syer
 * 
 */
public class CashewUserAuthenticationConverter implements UserAuthenticationConverter
{

	private static final Logger logger = LogManager.getLogger(CashewUserAuthenticationConverter.class);

	private Collection<? extends GrantedAuthority> defaultAuthorities;

	private UserDetailsService userDetailsService;

	private UserService userService;

	/**
	 * Optional {@link UserDetailsService} to use when extracting an
	 * {@link Authentication} from the incoming map.
	 * 
	 * @param userDetailsService
	 *            the userDetailsService to set
	 */
	public void setUserDetailsService(UserDetailsService userDetailsService)
	{
		this.userDetailsService = userDetailsService;
	}

	public void setUserService(UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Default value for authorities if an Authentication is being created and
	 * the input has no data for authorities. Note that unless this property is
	 * set, the default Authentication created by
	 * {@link #extractAuthentication(Map)} will be unauthenticated.
	 * 
	 * @param defaultAuthorities
	 *            the defaultAuthorities to set. Default null.
	 */
	public void setDefaultAuthorities(String[] defaultAuthorities)
	{
		this.defaultAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(StringUtils.arrayToCommaDelimitedString(defaultAuthorities));
	}

	public Map<String, ?> convertUserAuthentication(Authentication authentication)
	{
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put(USERNAME, authentication.getName());
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty())
		{
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		return response;
	}

	public Authentication extractAuthentication(Map<String, ?> map)
	{

		logger.debug("input args: " + map);
		if (map.containsKey(USERNAME))
		{
			UserDetails user;
			Object principal = map.get(USERNAME);
			Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
			try
			{
				user = userDetailsService.loadUserByUsername((String) map.get(USERNAME));
				authorities = user.getAuthorities();
				principal = user;
			}
			catch (UsernameNotFoundException e)
			{
				logger.warn(e.getMessage());

				String name = (String) map.get("userName");
				// String userId = (String) map.get("userId");
				String providerClientId = (String) map.get("clientId");
				String provider = (String) map.get("provider");

				User u = new User();
				u.setUserId((String) map.get(USERNAME));
				u.setAuthProvider(provider);
				u.setAuthProviderClientId(providerClientId);
				u.setEmail(null);
				u.setName(name);
				// ArrayList<String> roles = new ArrayList<>();
				// roles.add("ROLE_USER");
				// u.setRoles(roles);
				logger.debug("creating user = " + u);

				userService.createUser(u);

				user = userDetailsService.loadUserByUsername(u.getUserId());
				authorities = user.getAuthorities();
				principal = user;
			}
			logger.debug("retrieved user = " + user);

			return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
		}
		return null;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map)
	{
		if (!map.containsKey(AUTHORITIES))
		{
			return defaultAuthorities;
		}
		Object authorities = map.get(AUTHORITIES);
		if (authorities instanceof String)
		{
			return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
		}
		if (authorities instanceof Collection)
		{
			return AuthorityUtils.commaSeparatedStringToAuthorityList(
					StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
		}
		throw new IllegalArgumentException("Authorities must be either a String or a Collection");
	}
}
