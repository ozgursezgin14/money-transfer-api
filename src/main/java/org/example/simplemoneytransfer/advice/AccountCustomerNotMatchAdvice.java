package org.example.simplemoneytransfer.advice;

import org.example.simplemoneytransfer.dto.ErrorDataDTO;
import org.example.simplemoneytransfer.exception.AccountCustomerNotMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AccountCustomerNotMatchAdvice {
    
	@ResponseBody
	@ExceptionHandler(AccountCustomerNotMatchException.class)
	ResponseEntity<ErrorDataDTO> accountCustomerNotMatchHandler(AccountCustomerNotMatchException ex)
	{
		return new ResponseEntity<>(new ErrorDataDTO(404, ex.getMessage()), HttpStatus.NOT_FOUND);
	}
}
