package com.ib.ib.exceptions;

public class RequestAlreadyProcessedException extends RuntimeException{
    public RequestAlreadyProcessedException(String message) {
        super(message);
    }

}
