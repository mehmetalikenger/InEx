package com.stage.inex.domain.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException(String message){
        super(message);
    }
}
