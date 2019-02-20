package com.bitcoin_bank.spring.exception;

public class WalletNotFoundException extends Throwable {
    public WalletNotFoundException(final String message) {
        super(message);
    }
}