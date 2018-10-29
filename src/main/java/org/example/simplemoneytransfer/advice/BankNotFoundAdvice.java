package org.example.simplemoneytransfer.advice;

import org.example.simplemoneytransfer.dto.ErrorDataDTO;
import org.example.simplemoneytransfer.exception.BankNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BankNotFoundAdvice {
    
	@ResponseBody
	@ExceptionHandler(BankNotFoundException.class)
	ResponseEntity<ErrorDataDTO> bankNotFoundHandler(BankNotFoundException ex)
	{
		return new ResponseEntity<>(new ErrorDataDTO(404, ex.getMessage()), HttpStatus.NOT_FOUND);
	}
}
