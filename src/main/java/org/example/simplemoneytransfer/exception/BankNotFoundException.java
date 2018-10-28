package org.example.simplemoneytransfer.exception;

public class BankNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public BankNotFoundException(Long id) {
		super("Could not find bank with id: " + id);
	}

}
