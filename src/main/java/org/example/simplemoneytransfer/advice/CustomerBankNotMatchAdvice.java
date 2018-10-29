package org.example.simplemoneytransfer.advice;

import org.example.simplemoneytransfer.exception.CustomerBankNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomerBankNotMatchAdvice {
    
	@ResponseBody
	@ExceptionHandler(CustomerBankNotMatchException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String bankNotFoundHandler(CustomerBankNotMatchException ex)
	{
		return ex.getMessage();
	}
}
