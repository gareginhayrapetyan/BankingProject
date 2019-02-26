package com.bitcoin_bank.backend.exception;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(final String message) {
        super(message);
    }
}