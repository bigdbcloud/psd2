package com.ibm.psd2.api.twilio.message.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

@Service
public class MessageServiceImpl implements MessageService
{

	@Autowired
	private MessageFactory messageFactory;

	@Override
	public Message sendMessage(String fromNumber, String toNumber, String message) throws TwilioRestException
	{

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("To", toNumber));
		params.add(new BasicNameValuePair("From", fromNumber));
		params.add(new BasicNameValuePair("Body", message));
		Message sms = messageFactory.create(params);
		return sms;
	}

}
