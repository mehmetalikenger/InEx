package com.stage.inex.domain.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message){

        super(message);
    }
}
