package com.ibm.psd2.oauth2server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.ibm.psd2.oauth2server.services.ClientDetailsServiceImpl;
import com.ibm.psd2.oauth2server.services.UserDetailsServiceImpl;

@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	ClientDetailsServiceImpl cds;

	@Autowired
	UserDetailsServiceImpl uds;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter()
	{
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("tambre");
		return converter;
	}

	@Bean
	public TokenStore tokenStore()
	{
		return new JwtTokenStore(accessTokenConverter());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception
	{
		oauthServer.tokenKeyAccess("isAnonymous() || permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Bean
	public ApprovalStore approvalStore()
	{
		ApprovalStore store = new InMemoryApprovalStore();
		return store;
	}

	@Bean
	public InMemoryAuthorizationCodeServices authorizationCodeServices()
	{
		InMemoryAuthorizationCodeServices imacs = new InMemoryAuthorizationCodeServices();
		return imacs;
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices()
	{
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);

		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
		defaultTokenServices.setClientDetailsService(cds);
		return defaultTokenServices;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.withClientDetails(cds);
	}

	// @Bean
	// public CustomUserApprovalHandler userApprovalHandler()
	// {
	// CustomUserApprovalHandler handler = new CustomUserApprovalHandler();
	// handler.setApprovalStore(approvalStore());
	// handler.setClientDetailsService(cds);
	// handler.setUseApprovalStore(true);
	// handler.setRequestFactory(new DefaultOAuth2RequestFactory(cds));
	// return handler;
	// }

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	{
		endpoints.tokenServices(tokenServices()).approvalStore(approvalStore())
				.authorizationCodeServices(authorizationCodeServices());

		// endpoints.tokenServices(tokenServices()).accessTokenConverter(accessTokenConverter()).authenticationManager(authenticationManager)
		// .userDetailsService(uds).userApprovalHandler(userApprovalHandler())
		// .authorizationCodeServices(authorizationCodeServices());
	}

}
