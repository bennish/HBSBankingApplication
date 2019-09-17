package org.hbennis.arolla.bankAccountKata.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hbennis.arolla.bankAccountKata.exception.UnsupportedCurrencyException;
import org.hbennis.arolla.bankAccountKata.util.Constantes;

public class Amount implements Serializable {

	private static final long serialVersionUID = -5452984674598496399L;

	
	private BigDecimal value ; 
	private String currency ;
	

	public Amount(BigDecimal value) {
		this.value = value ;
		this.currency = Constantes.DEFAULT_CURRENCY ;
	}

	public Amount(BigDecimal value, String currency) throws UnsupportedCurrencyException {
		this.value = value ;
		this.currency = currency ;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	} 
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String toString() {
		return value.setScale(2, RoundingMode.HALF_DOWN) + " " + currency ;
	}
	
}
