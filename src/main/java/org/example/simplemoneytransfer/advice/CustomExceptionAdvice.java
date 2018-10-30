package org.example.simplemoneytransfer.advice;

import org.example.simplemoneytransfer.dto.ErrorDataDTO;
import org.example.simplemoneytransfer.exception.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomExceptionAdvice {
    
	@ResponseBody
	@ExceptionHandler(CustomException.class)
	ResponseEntity<ErrorDataDTO> customExceptionHandler(CustomException ex)
	{
		return new ResponseEntity<>(new ErrorDataDTO(ex.getStatus().value(), ex.getMessage()), ex.getStatus());
	}
}
