package com.bitcoin_bank.spring.interfaces;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.exception.InsufficientFundsException;

import java.math.BigDecimal;

public interface ITransactionManager {
    BankMessage verifyFundsSending(String senderAddress, String receiverAddress, String transactionAmount);

    BankMessage sendRequest(String walletAddress, BigDecimal requestAmount, String email);
}
