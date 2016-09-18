package com.ibm.psd2.api.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;

@Configuration
public class TwiloConfig {
	
	@Value("${twilio.account.sid}")
	private String accountSID;
	
	@Value("${twilio.authToken}")
	private String authToken;

	@Bean
	public TwilioRestClient getTwilioRestClient(){
				
		return new TwilioRestClient(accountSID,authToken);			
	}
	
	@Bean
	public Account getTwilioACcount(){
		
		return getTwilioRestClient().getAccount();
	}
	
	@Bean
	public MessageFactory getTwilioMessageFactory(){
		
		return getTwilioRestClient().getAccount().getMessageFactory();
		
	}
}
