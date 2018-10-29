package org.example.simplemoneytransfer.exception;

public class AccountCustomerNotMatchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public AccountCustomerNotMatchException(Long id, Long customerId) {
		super(String.format("Could not match account with id: %d and customer with id: %d", id, customerId));
	}

}
