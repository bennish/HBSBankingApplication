package org.hbennis.arolla.bankAccountKata.util;

import org.hbennis.arolla.bankAccountKata.exception.UnsupportedCurrencyException;
import org.hbennis.arolla.bankAccountKata.model.Amount;

public class CurrencyConverter {

	
	public static Amount convert(Amount value, String toCurrency) throws UnsupportedCurrencyException {
		if((toCurrency== null) || (value.getCurrency().equals(toCurrency)))
			return value ;

		else
			throw new UnsupportedCurrencyException();
	}
	
	
}
