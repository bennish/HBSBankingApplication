package org.hbennis.arolla.bankAccountKata.exception;

import java.io.File;

public class DatabaseAccessException extends Exception {

	private static final long serialVersionUID = 1L;


	public DatabaseAccessException(File file, Exception e) {
		super("Error accessing data in file " + file.getAbsolutePath(),e);
	}
}
