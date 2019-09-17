package org.hbennis.arolla.bankAccountKata.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {

	
	private static final long serialVersionUID = -85189644946657128L;
	
	private String accountId ;
	private String owner ; 
	private String currency ; 
	private BigDecimal balance ;
	private List<Statement> statementsHistory = new ArrayList<Statement>();

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<Statement> getStatementsHistory() {
		return statementsHistory;
	}

	public void addStatementToHistory(Statement statement) {
		this.statementsHistory.add(statement);
	}
	
	@Override
	public String toString() {
		String res = "Bank account num : " + accountId + "(" + owner +  ") \n" ;
		for(Statement st : statementsHistory)
			res += "\t"+ st +"\n" ;
		return res ;
	}
	
}
