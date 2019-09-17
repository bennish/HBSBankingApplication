package org.hbennis.arolla.bankAccountKata.service;

import org.hbennis.arolla.bankAccountKata.exception.BankingException;
import org.hbennis.arolla.bankAccountKata.exception.DatabaseAccessException;
import org.hbennis.arolla.bankAccountKata.exception.UnsupportedCurrencyException;
import org.hbennis.arolla.bankAccountKata.model.Account;

public interface DatabaseService {

	boolean accountExists(String accountId) ;
	
	public Account getAccount(String accountId) throws DatabaseAccessException, BankingException ;
	
	public void saveAccount(Account account) throws DatabaseAccessException, UnsupportedCurrencyException, BankingException  ;

	public void createAccount(Account account) throws DatabaseAccessException, UnsupportedCurrencyException, BankingException ;
	
	public void deleteAccount(String accountId) throws DatabaseAccessException, BankingException ;

}
