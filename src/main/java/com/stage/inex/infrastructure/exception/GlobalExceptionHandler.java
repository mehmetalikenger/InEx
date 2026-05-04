package com.stage.inex.infrastructure.exception;

import com.stage.inex.domain.exception.EmailAlreadyTakenException;
import com.stage.inex.domain.exception.InvalidJwtException;
import com.stage.inex.domain.exception.PasswordsDoNotMatchException;
import com.stage.inex.domain.exception.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<HashMap<String, HashMap<String, String>>> handleEmailAlreadyTaken(EmailAlreadyTakenException ex){

        HashMap<String, String> errorMessage = new HashMap<>();
        HashMap<String, HashMap<String, String>> errorDetails = new HashMap<>();

        errorMessage.put("message", ex.getMessage());
        errorDetails.put("email", errorMessage);

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, HashMap<String, String>>> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex){

        HashMap<String, HashMap<String, String>> errorDetails = new HashMap<>();

        for(FieldError error : ex.getFieldErrors()){

            HashMap<String, String> errorMessage = new HashMap<>();
            errorMessage.put("message", error.getDefaultMessage());
            errorDetails.put(error.getField(), errorMessage);
        }

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HashMap<String, HashMap<String, String>>> handleUserNotFoundException(RuntimeException ex){

        HashMap<String, String> errorMessage = new HashMap<>();
        HashMap<String, HashMap<String, String>> errorDetails = new HashMap<>();

        errorMessage.put("message", ex.getMessage());
        errorDetails.put("email", errorMessage);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<HashMap<String, HashMap<String, String>>> handlePasswordsDoNotMatchException(RuntimeException ex){

        HashMap<String, String> errorMessage = new HashMap<>();
        HashMap<String, HashMap<String, String>> errorDetails = new HashMap<>();

        errorMessage.put("message", ex.getMessage());
        errorDetails.put("password", errorMessage);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HashMap<String, HashMap<String, String>>> handleIllegalArgumentException(IllegalArgumentException ex){

        HashMap<String, String> errorMessage = new HashMap<>();
        HashMap<String, HashMap<String, String>> errorDetails = new HashMap<>();

        errorMessage.put("message", ex.getMessage());
        errorDetails.put("password", errorMessage);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
