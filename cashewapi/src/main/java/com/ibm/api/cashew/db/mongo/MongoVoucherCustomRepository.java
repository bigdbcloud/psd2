package com.ibm.api.cashew.db.mongo;

import com.ibm.api.cashew.beans.TxnDetails;
import com.ibm.api.cashew.beans.Voucher;

public interface MongoVoucherCustomRepository {

	public int updateVocher(Voucher vocher,TxnDetails redeemTo);
}
