package org.hbennis.arolla.bankAccountKata;

import java.math.BigDecimal;

import org.assertj.core.api.JUnitSoftAssertions;
import org.hbennis.arolla.bankAccountKata.controller.BankAccountController;
import org.hbennis.arolla.bankAccountKata.exception.BankingException;
import org.hbennis.arolla.bankAccountKata.exception.UnsupportedCurrencyException;
import org.hbennis.arolla.bankAccountKata.model.Amount;
import org.hbennis.arolla.bankAccountKata.service.FileDatabaseServiceImpl;
import org.hbennis.arolla.bankAccountKata.util.Constantes;
import org.hbennis.arolla.bankAccountKata.util.IdGenerator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BankAccountKataApplicationTests {

	@Rule
	public JUnitSoftAssertions should = new JUnitSoftAssertions();

	@Autowired
    private BankAccountController bankAccountController ;
	
	@Autowired
    private FileDatabaseServiceImpl databaseService ;
	
	//Creating Bank account
	@Test
	public void checkCreatingAccountCreatesEmptyAccount() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		should.assertThat(databaseService.getAccount(id)).isNotNull();
	}
	@Test(expected = BankingException.class)
	public void checkCannotCreateAccountWithoutOwner() throws Exception {
		bankAccountController.createAccount("", "EUR") ;
	}
	@Test(expected = UnsupportedCurrencyException.class)
	public void checkOnlyEuroAccountsCanBeCreated() throws Exception {
		bankAccountController.createAccount("Halima BENNIS", "USD") ;
	}

	//Accessing Bank Account
	@Test(expected = BankingException.class)
	public void checkNonExistingAccountThrowsException() throws Exception {
		String id = IdGenerator.generateId() ;
		while(databaseService.accountExists(id)) {
			id = IdGenerator.generateId() ;
		}
		databaseService.getAccount(id);
	}
	
	// Deposit
	@Test
	public void checkDepositCreditedToAccount() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(50)));
		should.assertThat(databaseService.getAccount(id).getBalance()).isEqualTo(new BigDecimal(50));
	}
	
	@Test(expected = BankingException.class)
	public void checkCantDepositNegativeAmount() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(-20)));
	}
	@Test(expected = UnsupportedCurrencyException.class)
	public void checkOnlyEuroCurrencyDepositIsSupported() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(50), "CAD"));
	}
	
	// Withdraw
	@Test
	public void checkWithdrawalDebitedFromAccount() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(50)));
		bankAccountController.withdraw(id, new Amount(new BigDecimal(20)));
		should.assertThat(databaseService.getAccount(id).getBalance()).isEqualTo(new BigDecimal(50).subtract(new BigDecimal(20)));
	
	}
	@Test(expected = BankingException.class)
	public void checkCantWithdrawIfNegativeBalance() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(40)));
		bankAccountController.withdraw(id, new Amount(new BigDecimal(50)));
	}
	@Test(expected = UnsupportedCurrencyException.class)
	public void checkOnlyEuroCurrencyWithdrawalIsSupported() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(50), "EUR"));
		bankAccountController.withdraw(id, new Amount(new BigDecimal(20), "USD"));
	}
	
	// Statement
	@Test
	public void checkBankOperationAddsCorrectStatementType() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(0).getStatementType()).isEqualTo(Constantes.STATEMENT_TYPE_CREAT);
		bankAccountController.deposit(id, new Amount(new BigDecimal(50), "EUR"));
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(1).getStatementType()).isEqualTo(Constantes.STATEMENT_TYPE_DEPOS);
		bankAccountController.withdraw(id, new Amount(new BigDecimal(20), "EUR"));
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(2).getStatementType()).isEqualTo(Constantes.STATEMENT_TYPE_WITHD);
	}
	
	@Test
	public void checkBankOperationAddsCorrectBeforeAndAfterBalance() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(50), "EUR"));
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(1).getBalanceBefore()).isEqualTo(BigDecimal.ZERO);
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(1).getBalanceAfter()).isEqualTo(new BigDecimal(50));
		bankAccountController.withdraw(id, new Amount(new BigDecimal(20), "EUR"));
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(2).getBalanceBefore()).isEqualTo(new BigDecimal(50));
		should.assertThat(databaseService.getAccount(id).getStatementsHistory().get(2).getBalanceAfter()).isEqualTo(new BigDecimal(50).subtract(new BigDecimal(20)));
	}
	
	@Test
	public void checkPrintingAccountPrints1LinePlus3Statements() throws Exception {
		String id = bankAccountController.createAccount("Halima BENNIS", "EUR") ;
		bankAccountController.deposit(id, new Amount(new BigDecimal(50), "EUR"));
		bankAccountController.withdraw(id, new Amount(new BigDecimal(20), "EUR"));
		System.out.println(databaseService.getAccount(id).toString());
		should.assertThat(databaseService.getAccount(id).toString().split("\n").length).isEqualTo(4);
		
	}
}
