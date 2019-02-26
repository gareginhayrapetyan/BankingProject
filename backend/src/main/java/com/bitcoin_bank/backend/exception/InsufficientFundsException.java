package com.bitcoin_bank.backend.exception;

public class InsufficientFundsException extends Throwable {
    public InsufficientFundsException(final String message) {
        super(message);
    }
}