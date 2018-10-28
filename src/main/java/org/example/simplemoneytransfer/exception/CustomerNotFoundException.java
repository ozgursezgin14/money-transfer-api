package org.example.simplemoneytransfer.exception;

public class CustomerNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(Long id) {
		super("Could not find customer with id: " + id);
	}

}
