package com.bitcoin_bank.backend.exception;

public class WalletNotFoundException extends Throwable {
    public WalletNotFoundException(final String message) {
        super(message);
    }
}