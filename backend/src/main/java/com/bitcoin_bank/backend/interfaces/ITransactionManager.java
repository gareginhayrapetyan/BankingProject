package com.bitcoin_bank.backend.interfaces;

import aca.proto.BankMessage;
import com.bitcoin_bank.backend.exception.WalletNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionManager {
    BankMessage verifyFundsSending(String senderAddress, String receiverAddress, String transactionAmount);

    BankMessage sendRequest(String walletAddress, BigDecimal requestAmount, String email);

    List<BankMessage.ReceivedTransactionMsg> getReceivedTransactionsHistory(String receiverAddress) throws WalletNotFoundException;

    List<BankMessage.SentTransactionMsg> getTransactionsSentHistory(String senderAddress) throws WalletNotFoundException;

    List<BankMessage> getAllTransactions(String address) throws WalletNotFoundException;
}
