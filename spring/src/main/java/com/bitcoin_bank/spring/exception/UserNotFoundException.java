package com.bitcoin_bank.spring.exception;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(final String message) {
        super(message);
    }
}