package org.example.simplemoneytransfer.exception;

public class AccountNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public AccountNotFoundException(Long id) {
		super("Could not find account with id:" + id);
	}

}
