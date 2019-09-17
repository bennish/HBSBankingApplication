package org.hbennis.arolla.bankAccountKata.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.hbennis.arolla.bankAccountKata.exception.BankingException;
import org.hbennis.arolla.bankAccountKata.exception.DatabaseAccessException;
import org.hbennis.arolla.bankAccountKata.exception.UnsupportedCurrencyException;
import org.hbennis.arolla.bankAccountKata.model.Account;
import org.hbennis.arolla.bankAccountKata.util.Constantes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileDatabaseServiceImpl implements DatabaseService {
	
	@Value(value = "${app.dataFolder}")
	private String dataFolder;
	
	private void checkDataFolder() {
		File folder = new File(dataFolder) ;
		if(!folder.exists())
			folder.mkdir();
	}
	
	public void setDataFolder(String dataFolder) {
		this.dataFolder = dataFolder;
		checkDataFolder();
	}

	@Override
	public boolean accountExists(String accountId) {
		File f = new File(dataFolder, accountId + ".ser") ;	
		return f.exists();
	}

	@Override
	public Account getAccount(String accountId) throws DatabaseAccessException, BankingException {
		if(!accountExists(accountId))
			throw new BankingException("This account doesn't exist : " + accountId) ;
		File f = new File(dataFolder, accountId + ".ser") ;
		ObjectInputStream ois = null ;
		Account acc = null ;
		try {
			ois =  new ObjectInputStream(new FileInputStream(f)) ;
			acc = (Account) ois.readObject() ;
		} catch (Exception e) {
			throw new DatabaseAccessException(f, e) ;
		} finally {
			if(ois != null) {
				try { 
					ois.close();
				} catch (Exception e) {}
			}
		}
		if(acc == null)
			throw new BankingException("Corrupted Account : " + accountId) ;
		return acc; 
	}

	@Override
	public void saveAccount(Account account) throws DatabaseAccessException, UnsupportedCurrencyException, BankingException {
		if(account == null || account.getAccountId() == null)
			throw new BankingException("Can't save an undefined bank account.");
		if(!Constantes.DEFAULT_CURRENCY.equals(account.getCurrency()))
			throw new UnsupportedCurrencyException();

		if (!accountExists(account.getAccountId()))
			throw new BankingException("Unknown Account : " + account.getAccountId()) ;

		File f = new File(dataFolder, account.getAccountId() + ".ser") ;
		ObjectOutputStream oos = null ;
		try {
			oos =  new ObjectOutputStream(new FileOutputStream(f)) ;
			oos.writeObject(account) ;
		} catch (Exception e) {
			throw new DatabaseAccessException(f, e);
		} finally {
			if(oos != null) {
				try {
					oos.close();
				}catch(Exception e) {}
			}
		}
	}


	@Override
	public void createAccount(Account account) throws DatabaseAccessException, UnsupportedCurrencyException, BankingException {
		if(account == null || account.getAccountId() == null)
			throw new BankingException("Can't save an undefined bank account.");
		if(!Constantes.DEFAULT_CURRENCY.equals(account.getCurrency()))
			throw new UnsupportedCurrencyException();

		if (accountExists(account.getAccountId()))
			throw new BankingException("Account already exists : " + account.getAccountId()) ;
		checkDataFolder();
		File f = new File(dataFolder, account.getAccountId() + ".ser") ;
		ObjectOutputStream oos = null ;
		try {
			oos =  new ObjectOutputStream(new FileOutputStream(f)) ;
			oos.writeObject(account) ;
		} catch (Exception e) {
			throw new DatabaseAccessException(f, e);
		} finally {
			if(oos != null) {
				try {
					oos.close();
				}catch(Exception e) {}
			}
		}

	}

	@Override
	public void deleteAccount(String accountId) throws DatabaseAccessException, BankingException {
		if(accountId == null)
			throw new BankingException("Can't delete an undefined bank account.");
		if (!accountExists(accountId))
			throw new BankingException("Unknown Account : " + accountId) ;
		Account account = getAccount(accountId);
		if(account == null)
			throw new BankingException("Corrupted Account : " + accountId) ;
		File f = new File(dataFolder, account.getAccountId() + ".ser") ;
		try {
			if(!f.delete())
				throw new DatabaseAccessException(f, new Exception("Can't delete account file."));
		} catch (Exception e) {
			throw new DatabaseAccessException(f, e);
		}
	}
	
	
	
}
