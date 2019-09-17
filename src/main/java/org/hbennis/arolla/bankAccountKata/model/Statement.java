package org.hbennis.arolla.bankAccountKata.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hbennis.arolla.bankAccountKata.util.Constantes;

public class Statement implements Serializable {

	private static final long serialVersionUID = -641436705497308166L;
	
	private Amount amount ; 
	private Date date ; 
	private BigDecimal balanceBefore ; 
	private BigDecimal balanceAfter ;
	private String statementType ;
	private String accountCurrency ;
	
	public Statement(Amount amount, Date date, BigDecimal balanceBefore, BigDecimal balanceAfter, String statementType, String accountCurrency) {
		this.amount = amount ;
		this.date = date ;
		this.balanceBefore = balanceBefore;
		this.balanceAfter = balanceAfter;
		this.statementType = statementType;
		this.accountCurrency = accountCurrency;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Amount getAmount() {
		return amount;
	}
	public void setAmount(Amount amount) {
		this.amount = amount;
	}
	public BigDecimal getBalanceBefore() {
		return balanceBefore;
	}
	public void setBalanceBefore(BigDecimal balanceBefore) {
		this.balanceBefore = balanceBefore;
	}
	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}
	public void setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	public String getStatementType() {
		return statementType;
	}
	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.STATEMENT_DATE_FORMAT) ;
		return 
				"[" + sdf.format(date)  + "] "
				+ "[" + statementType  + "] "
				+ "Amount : " + amount + ", "
				+ "Balance : " + balanceBefore + " " + accountCurrency
				+ " --> " + balanceAfter + " " + accountCurrency ; 
	}
}
