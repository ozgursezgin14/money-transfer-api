package org.example.simplemoneytransfer.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;
	public CustomException(String msg, HttpStatus status)
	{
		super(msg);
		if (status == null)
			throw new NullPointerException("status");
		this.status = status;
	}
	
	public CustomException(String msg, Throwable cause, HttpStatus status)
	{
		super(msg, cause);
		if (status == null)
			throw new NullPointerException("status");
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
	
}
