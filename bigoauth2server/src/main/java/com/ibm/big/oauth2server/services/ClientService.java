package com.ibm.big.oauth2server.services;

import com.ibm.big.oauth2server.beans.Client;

public interface ClientService
{

	public Client findById(String id);
	
	public Client createClient(Client client);
}
