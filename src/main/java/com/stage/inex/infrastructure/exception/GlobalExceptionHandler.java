package com.stage.inex.infrastructure.exception;

import com.stage.inex.domain.exception.EmailAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.util.JSONPObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyTaken(EmailAlreadyTakenException ex){

        Map<String, String> errorDetails = new HashMap<>();

        errorDetails.put("message", ex.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, HashMap<String, String>>> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex){

        HashMap<String, HashMap<String, String>> errorDetail = new HashMap<>();

        for(FieldError error : ex.getFieldErrors()){

            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("message", error.getDefaultMessage());
            errorDetail.put(error.getField(), errorMessage);
        }

        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
