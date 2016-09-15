package com.ibm.psd2.api;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ibm.psd2.api.subscription.db", "com.ibm.psd2.api.subscription.service",
		"com.ibm.psd2.api.user.db", "com.ibm.psd2.api.user.service",
		"com.ibm.psd2.api.admin.controller", "com.ibm.psd2.api.aip.controller", "com.ibm.psd2.api.aip.db",
		"com.ibm.psd2.api.aip.services", "com.ibm.psd2.api.integration", "com.ibm.psd2.api.pisp.controller",
		"com.ibm.psd2.api.pisp.db", "com.ibm.psd2.api.pisp.service", "com.ibm.psd2.api.txns.service",
		"com.ibm.psd2.api.security", "com.ibm.psd2.api.subscription.controller", "com.ibm.psd2.api.swagger",
		"com.ibm.psd2.api.utils" })
public class Psd2ApiApp
{
	@RequestMapping("/user")
	public Principal user(Principal user)
	{
		return user;
	}

	public static void main(String[] args)
	{
		SpringApplication.run(Psd2ApiApp.class, args);
	}

}
