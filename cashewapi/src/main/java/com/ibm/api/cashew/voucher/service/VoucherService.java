package com.ibm.api.cashew.voucher.service;

import com.ibm.api.cashew.beans.Voucher;

public interface VoucherService {

	public Voucher createVocher(Voucher vocher);
	public Voucher redeemVocher(Voucher vocher);
}
