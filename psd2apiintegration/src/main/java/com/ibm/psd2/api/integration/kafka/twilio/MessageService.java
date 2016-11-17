package com.ibm.psd2.api.integration.kafka.twilio;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Message;

public interface MessageService
{

	public Message sendMessage(String fromNumber, String toNumber, String message) throws TwilioRestException;
}
