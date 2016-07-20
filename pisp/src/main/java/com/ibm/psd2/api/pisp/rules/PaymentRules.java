package com.ibm.psd2.api.pisp.rules;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ibm.psd2.api.commons.Constants;
import com.ibm.psd2.commons.beans.AmountBean;
import com.ibm.psd2.commons.beans.ChallengeAnswerBean;
import com.ibm.psd2.commons.beans.pisp.TxnChargeBean;
import com.ibm.psd2.commons.beans.pisp.TxnPartyBean;
import com.ibm.psd2.commons.beans.pisp.TxnRequestBean;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfoBean;
import com.ibm.psd2.commons.beans.subscription.TransactionLimitBean;
import com.ibm.psd2.commons.beans.subscription.TransactionRequestTypeBean;

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
