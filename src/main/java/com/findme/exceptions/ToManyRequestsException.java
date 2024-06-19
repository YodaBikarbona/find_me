package com.findme.exceptions;

public class ToManyRequestsException extends RuntimeException {

    public ToManyRequestsException(String message) {
        super(message);
    }
}
