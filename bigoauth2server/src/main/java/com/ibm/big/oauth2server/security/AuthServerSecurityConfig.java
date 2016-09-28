package com.ibm.big.oauth2server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthServerSecurityConfig extends AuthorizationServerConfigurerAdapter
{
	@Value("${bigoauth2server.oauth2.signingKey}")
	private String tokenSigningKey;

	@Autowired
	ClientDetailsServiceImpl cds;

	@Bean
	public BigOauth2ServerTokenEnhancer accessTokenConverter()
	{
		BigOauth2ServerTokenEnhancer converter = new BigOauth2ServerTokenEnhancer();
		converter.setSigningKey(tokenSigningKey);
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
		defaultTokenServices.setAccessTokenValiditySeconds(3600);
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setRefreshTokenValiditySeconds(86400);
		defaultTokenServices.setTokenEnhancer(accessTokenConverter());
		defaultTokenServices.setSupportRefreshToken(true);
		return defaultTokenServices;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.withClientDetails(cds);
		// clients.inMemory().withClient("codertalk").secret("password01")
		// .authorizedGrantTypes("authorization_code", "refresh_token")
		// .authorities("ROLE_TRUSTED_CLIENT", "ROLE_USER")
		// .scopes("codertalkapp").accessTokenValiditySeconds(3600)
		// .refreshTokenValiditySeconds(86400);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	{
		endpoints.tokenServices(tokenServices()).approvalStore(approvalStore())
				.authorizationCodeServices(authorizationCodeServices());

		// endpoints.tokenStore(tokenStore()).accessTokenConverter(accessTokenConverter()).approvalStore(approvalStore())
		// .authorizationCodeServices(authorizationCodeServices());
		// .authenticationManager(authenticationManager).userDetailsService(uds)
		// .userApprovalHandler(userApprovalHandler()).authorizationCodeServices(authorizationCodeServices());
	}
}