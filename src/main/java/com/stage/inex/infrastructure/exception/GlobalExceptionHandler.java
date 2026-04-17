package com.stage.inex.infrastructure.exception;

import com.stage.inex.domain.exception.EmailAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyTaken(EmailAlreadyTakenException ex){

        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("message", ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
