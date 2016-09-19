package com.ibm.psd2.api;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ibm.psd2.api"})
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
