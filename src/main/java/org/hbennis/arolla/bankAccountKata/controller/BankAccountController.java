package org.hbennis.arolla.bankAccountKata.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import org.hbennis.arolla.bankAccountKata.exception.BankingException;
import org.hbennis.arolla.bankAccountKata.exception.DatabaseAccessException;
import org.hbennis.arolla.bankAccountKata.exception.UnsupportedCurrencyException;
import org.hbennis.arolla.bankAccountKata.model.Account;
import org.hbennis.arolla.bankAccountKata.model.Amount;
import org.hbennis.arolla.bankAccountKata.model.Statement;
import org.hbennis.arolla.bankAccountKata.service.FileDatabaseServiceImpl;
import org.hbennis.arolla.bankAccountKata.util.Constantes;
import org.hbennis.arolla.bankAccountKata.util.CurrencyConverter;
import org.hbennis.arolla.bankAccountKata.util.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class BankAccountController {

	
	@Inject
	FileDatabaseServiceImpl databaseService ;
	
	public String createAccount(String owner, String currency) throws DatabaseAccessException, UnsupportedCurrencyException, BankingException {
		if(!Constantes.DEFAULT_CURRENCY.equals(currency))
			throw new UnsupportedCurrencyException();
		if(owner == null || "".equals(owner))
			throw new BankingException("An account must have a valid owner.");
		Account acc = new Account();
		acc.setOwner(owner);
		acc.setCurrency(currency);
		acc.setBalance(BigDecimal.ZERO);
		ArrayList<Statement> list = new ArrayList<Statement>();
		Statement st = new Statement(
					new Amount(BigDecimal.ZERO, currency), 
					new Date(), 
					BigDecimal.ZERO, 
					BigDecimal.ZERO, 
					Constantes.STATEMENT_TYPE_CREAT, 
					currency) ; 
		list.add(st) ;
		acc.addStatementToHistory(st);
		
		String accountId = IdGenerator.generateId(); 
		while (databaseService.accountExists(accountId)) {
			accountId = IdGenerator.generateId();
		}
		acc.setAccountId(accountId);
		databaseService.createAccount(acc);
		return accountId;
	}

	public void deleteAccount(String accountId) throws DatabaseAccessException, BankingException {
		
		Account acc = databaseService.getAccount(accountId) ;

		if(acc.getBalance() == null || acc.getBalance().compareTo(BigDecimal.ZERO) != 0)
			throw new BankingException("Can't delete an non empty or negative balance account.");
		
		databaseService.deleteAccount(accountId);
	}

	
	public Amount withdraw(String accountId, Amount amount) throws DatabaseAccessException, BankingException, UnsupportedCurrencyException {
		Account acc = databaseService.getAccount(accountId) ;
		BigDecimal balance = acc.getBalance() ;
		Amount convertedAmount = CurrencyConverter.convert(amount, acc.getCurrency());
		BigDecimal newBalance = balance.subtract(convertedAmount.getValue()) ;
		if(newBalance.compareTo(BigDecimal.ZERO) < 0)
			throw new BankingException("You don't have enough money on your bank account to withdraw this amount.");
		acc.setBalance(newBalance);
		acc.getStatementsHistory().add(new Statement(convertedAmount, new Date(), balance, newBalance, Constantes.STATEMENT_TYPE_WITHD, acc.getCurrency())) ;
		databaseService.saveAccount(acc) ;
		return new Amount(newBalance);
	}

	public Amount deposit(String accountId, Amount amount) throws DatabaseAccessException, BankingException, UnsupportedCurrencyException {
		if(amount.getValue().compareTo(BigDecimal.ZERO)==-1)
			throw new BankingException("You can't deposit a negative amount on your account");
		Account acc = databaseService.getAccount(accountId) ;
		BigDecimal balance = acc.getBalance() ;
		Amount convertedAmount = CurrencyConverter.convert(amount, acc.getCurrency());
		BigDecimal newBalance = balance.add(convertedAmount.getValue()) ;
		acc.setBalance(newBalance);
		acc.getStatementsHistory().add(new Statement(convertedAmount, new Date(), balance, newBalance, Constantes.STATEMENT_TYPE_DEPOS, acc.getCurrency())) ;
		databaseService.saveAccount(acc) ;
		return new Amount(newBalance);
	}

	
	public String printHistory(String accountId) throws DatabaseAccessException, BankingException {
		return  databaseService.getAccount(accountId).toString();
	}

	
}
