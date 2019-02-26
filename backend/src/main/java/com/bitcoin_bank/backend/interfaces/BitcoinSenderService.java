package com.bitcoin_bank.backend.interfaces;

import aca.proto.BankMessage;

import java.math.BigDecimal;

public interface BitcoinSenderService {
    BankMessage withdrawFromWalletToWallet(String fromAddress, String toAddress, BigDecimal amount);
}
