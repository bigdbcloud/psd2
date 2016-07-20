package com.ibm.psd2.api.subscriptionmgmt;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ibm.psd2.api.commons.db", "com.ibm.psd2.api.commons.integration",
		"com.ibm.psd2.api.subscription.dao", "com.ibm.psd2.api.subscription.controller",
		"com.ibm.psd2.api.subscriptionmgmt.controller", "com.ibm.psd2.api.swagger" })
public class Psd2SubscriptionMgmtApiApp {
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	public static void main(String[] args) {
		SpringApplication.run(Psd2SubscriptionMgmtApiApp.class, args);
	}

}
