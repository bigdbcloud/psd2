package com.ibm.api.cashew.vocher.service;

import com.ibm.api.cashew.beans.Vocher;

public interface VocherService {

	public Vocher createVocher(Vocher vocher);
	public Vocher redeemVocher(Vocher vocher);
}
