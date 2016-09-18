package com.ibm.api.cashew.vocher.db;

import com.ibm.api.cashew.beans.TxnDetails;
import com.ibm.api.cashew.beans.Vocher;

public interface MongoVocherCustomRepository {

	public int updateVocher(Vocher vocher,TxnDetails redeemTo);
}
