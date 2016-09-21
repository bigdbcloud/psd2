package com.ibm.psd2.api.pisp.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.psd2.api.pisp.service.CounterPartyService;
import com.ibm.psd2.datamodel.pisp.CounterParty;

@RestController
public class CounterPartyController
{
	private final Logger logger = LogManager.getLogger(CounterPartyController.class);

	@Autowired
	CounterPartyService cps;
	
	@PreAuthorize("hasPermission(#user + '.' + #bankId + '.' + #accountId + '.' + #viewId, 'getAccountInfo')")
	@RequestMapping(method = RequestMethod.GET, value = "banks/{bankId}/accounts/{accountId}/{viewId}/counter-parties", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<CounterParty>> getCounterParties(
			@PathVariable("bankId") String bankId, @PathVariable("accountId") String accountId,
			@PathVariable("viewId") String viewId,
			@RequestHeader(value = "user", required = true) String user)
	{
		ResponseEntity<List<CounterParty>> response;
		try
		{
			List<CounterParty> txns = cps.getCounterParties(bankId, accountId);
			response = ResponseEntity.ok(txns);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			response = ResponseEntity.badRequest().body(null);
		}
		return response;
	}

}
