package com.ibm.api.cashew.services.ibmbank;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ibm.api.cashew.beans.UserAccount;
import com.ibm.psd2.datamodel.pisp.TxnRequest;
import com.ibm.psd2.datamodel.pisp.TxnRequestDetails;

@Service
public class IBMPaymentsServiceImpl implements IBMPaymentsService
{
	private Logger logger = LogManager.getLogger(IBMPaymentsServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IBMPSD2Credentials psd2Credentials;

	@Override
	public TxnRequestDetails createTransactionRequest(UserAccount ua, TxnRequest trb, String txnType) throws Exception
	{
		TxnRequestDetails txnDetails = null;
		String url = psd2Credentials.getPsd2Url() + "/banks/" + ua.getAccount().getBankId() + "/accounts/"
				+ ua.getAccount().getId() + "/" + ua.getViewIds().get(0).getId() + "/transaction-request-types/"
				+ txnType + "/transaction-requests";
		logger.debug("url = " + url);

		URI uri = new URI(url);

		RequestEntity<TxnRequest> rea = RequestEntity.post(uri).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", psd2Credentials.getPSD2Authorization())
				.header("user", ua.getAccount().getUsername()).body(trb);

		ResponseEntity<TxnRequestDetails> res = restTemplate.exchange(rea, TxnRequestDetails.class);

		txnDetails = res.getBody();
		return txnDetails;
	}

}
