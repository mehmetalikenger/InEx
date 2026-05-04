package com.stage.inex.domain.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(String message){
        super(message);
    }
}
