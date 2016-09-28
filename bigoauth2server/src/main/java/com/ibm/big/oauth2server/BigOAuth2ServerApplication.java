package com.ibm.big.oauth2server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ibm.big.oauth2server.controller", "com.ibm.big.oauth2server.security",
		"com.ibm.big.oauth2server.db", "com.ibm.big.oauth2server.services", "com.ibm.big.oauth2server" })
public class BigOAuth2ServerApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(BigOAuth2ServerApplication.class, args);
	}
}
