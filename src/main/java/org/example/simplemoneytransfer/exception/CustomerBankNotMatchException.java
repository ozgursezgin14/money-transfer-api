package org.example.simplemoneytransfer.exception;

public class CustomerBankNotMatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public CustomerBankNotMatchException(Long id, Long bankId) {
		super(String.format("Could not match customer with id: %d and bank with id: %d", id, bankId));
	}

}
