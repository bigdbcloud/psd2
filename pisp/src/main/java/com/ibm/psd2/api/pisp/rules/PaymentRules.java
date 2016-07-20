package com.ibm.psd2.api.pisp.rules;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ibm.psd2.api.commons.Constants;
import com.ibm.psd2.commons.beans.Amount;
import com.ibm.psd2.commons.beans.ChallengeAnswer;
import com.ibm.psd2.commons.beans.pisp.TxnCharge;
import com.ibm.psd2.commons.beans.pisp.TxnParty;
import com.ibm.psd2.commons.beans.pisp.TxnRequest;
import com.ibm.psd2.commons.beans.subscription.SubscriptionInfo;
import com.ibm.psd2.commons.beans.subscription.TransactionLimit;
import com.ibm.psd2.commons.beans.subscription.TransactionRequestType;

@Component
public class PaymentRules
{

	public boolean isSubscribed(SubscriptionInfo sib)
	{
		if (sib == null)
		{
			return false;
		}
		return true;
	}

	public boolean isTransactionTypeAllowed(SubscriptionInfo sib, String txnType)
	{
		for (Iterator<TransactionRequestType> iterator = sib.getTransaction_request_types()
				.iterator(); iterator.hasNext();)
		{
			TransactionRequestType type = (TransactionRequestType) iterator.next();
			if (type.getValue().equals(txnType))
			{
				return true;
			}
		}
		return false;
	}

	public boolean checkLimit(TxnRequest trb, SubscriptionInfo sib, String txnType)
	{

		List<TransactionLimit> limits = sib.getLimits();

		for (Iterator<TransactionLimit> iterator = limits.iterator(); iterator.hasNext();)
		{
			TransactionLimit limit = iterator.next();

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
		if (TransactionRequestType.TYPES.WITHIN_BANK.type().equals(txnType)
				|| TransactionRequestType.TYPES.INTER_BANK.type().equals(txnType)
				|| TransactionRequestType.TYPES.INTERNATIONAL.type().equals(txnType))
		{
			return true;
		}
		return false;
	}

	public TxnCharge getTransactionCharge(TxnRequest trb,
			TxnParty payee)
	{
		Amount ab = new Amount();
		// Currently hardcoded charge
		ab.setAmount(1.00);
		ab.setCurrency(Constants.CURRENCY_GBP);

		TxnCharge tcb = new TxnCharge();
		tcb.setSummary("Transaction Charge Summary");
		tcb.setValue(ab);
		return tcb;
	}
	
	public boolean validateTxnChallengeAnswer(ChallengeAnswer t, String user, String accountId, String bankId)
	{
		if (t.getAnswer() != null)
		{
			return true;
		}
		return false;
	}

}
