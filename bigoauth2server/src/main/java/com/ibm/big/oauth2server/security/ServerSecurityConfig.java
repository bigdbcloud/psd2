package com.ibm.big.oauth2server.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableOAuth2Client
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter
{

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	UserDetailsServiceImpl uds;
	
	@Autowired
	SocialLoginAuthSuccessHandlerFactory slashFactory;

	@Bean
	@ConfigurationProperties("google")
	public ClientResources google()
	{
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebook()
	{
		return new ClientResources();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}	

	@Bean
	public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter)
	{
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	private Filter ssoFilter(ClientResources client, String path, String provider)
	{
		OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);
		OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
		filter.setRestTemplate(template);
		UserInfoTokenServices uits = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
				client.getClient().getClientId());
		uits.setRestTemplate(template);
		filter.setTokenServices(uits);
//		filter.setAuthenticationSuccessHandler(slashFactory.getAuthenticationSuccessHandler(provider));
		return filter;
	}

	private Filter ssoFilter()
	{
		CompositeFilter filter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();
		filters.add(ssoFilter(facebook(), "/login/facebook", "facebook"));
//		filters.add(ssoFilter(google(), "/login/google", "google"));
		filter.setFilters(filters);
		return filter;
	}

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(uds).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/oauth/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/user").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/client").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/provider").permitAll();
		http.authorizeRequests()
				.antMatchers("/images/**", "/login*.html", "/login/**", "/webjars/**", "/oauth/check_token")
				.permitAll().anyRequest().authenticated()
				.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login.html"))
				.and().formLogin().loginPage("/login.html").loginProcessingUrl("/login/bigoauth2server").failureUrl("/login.html")
				.and().logout().logoutSuccessUrl("/login.html").permitAll()
				.and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
				.csrf().disable(); //csrfTokenRepository(new CookieCsrfTokenRepository());
	}
}
