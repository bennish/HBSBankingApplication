package org.hbennis.arolla.bankAccountKata.exception;

public class UnsupportedCurrencyException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public UnsupportedCurrencyException() {
		super("Your account only supports EUR currency") ;
	}
}
