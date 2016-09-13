package com.ibm.psd2.api.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = { "com.ibm.psd2.commons.subscription.db", 
		"com.ibm.psd2.commons.subscription.service",
		"com.ibm.psd2.api.security",
		"com.ibm.psd2.api.subscription.controller",
		"com.ibm.psd2.api.swagger",
		"com.ibm.psd2.api.utils"
		})

public class Psd2ApiSubscription 
{
	public static void main(String[] args) {
        SpringApplication.run(Psd2ApiSubscription.class, args);
   }

}
