package com.ibm.api.psd2.api.pisp;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ibm.api.psd2.api.beans.AmountBean;
import com.ibm.api.psd2.api.beans.ChallengeAnswerBean;
import com.ibm.api.psd2.api.beans.payments.TxnChargeBean;
import com.ibm.api.psd2.api.beans.payments.TxnPartyBean;
import com.ibm.api.psd2.api.beans.payments.TxnRequestBean;
import com.ibm.api.psd2.api.beans.subscription.SubscriptionInfoBean;
import com.ibm.api.psd2.api.beans.subscription.TransactionLimitBean;
import com.ibm.api.psd2.api.beans.subscription.TransactionRequestTypeBean;
import com.ibm.api.psd2.api.common.Constants;

@Component
public class PaymentRules
{

	public boolean isSubscribed(SubscriptionInfoBean sib)
	{
		if (sib == null)
		{
			return false;
		}
		return true;
	}

	public boolean isTransactionTypeAllowed(SubscriptionInfoBean sib, String txnType)
	{
		for (Iterator<TransactionRequestTypeBean> iterator = sib.getTransaction_request_types()
				.iterator(); iterator.hasNext();)
		{
			TransactionRequestTypeBean type = (TransactionRequestTypeBean) iterator.next();
			if (type.getValue().equals(txnType))
			{
				return true;
			}
		}
		return false;
	}

	public boolean checkLimit(TxnRequestBean trb, SubscriptionInfoBean sib, String txnType)
	{

		List<TransactionLimitBean> limits = sib.getLimits();

		for (Iterator<TransactionLimitBean> iterator = limits.iterator(); iterator.hasNext();)
		{
			TransactionLimitBean limit = iterator.next();

			if (limit.getTransaction_request_type().getValue().equals(txnType)
					&& limit.getAmount().getCurrency().equals(trb.getValue().getCurrency())
					&& limit.getAmount().getAmount() > trb.getValue().getAmount())
			{
				return true;
			}
		}
		return false;
	}

	public boolean checkTxnType(String txnType)
	{
		if (TransactionRequestTypeBean.TYPES.WITHIN_BANK.type().equals(txnType)
				|| TransactionRequestTypeBean.TYPES.INTER_BANK.type().equals(txnType)
				|| TransactionRequestTypeBean.TYPES.INTERNATIONAL.type().equals(txnType))
		{
			return true;
		}
		return false;
	}

	public TxnChargeBean getTransactionCharge(TxnRequestBean trb,
			TxnPartyBean payee)
	{
		AmountBean ab = new AmountBean();
		// Currently hardcoded charge
		ab.setAmount(1.00);
		ab.setCurrency(Constants.CURRENCY_GBP);

		TxnChargeBean tcb = new TxnChargeBean();
		tcb.setSummary("Transaction Charge Summary");
		tcb.setValue(ab);
		return tcb;
	}
	
	public boolean validateTxnChallengeAnswer(ChallengeAnswerBean t, String user, String accountId, String bankId)
	{
		if (t.getAnswer() != null)
		{
			return true;
		}
		return false;
	}

}
