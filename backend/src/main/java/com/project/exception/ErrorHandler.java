package com.project.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode("400");
        errorDto.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }

}