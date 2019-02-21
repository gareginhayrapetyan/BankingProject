package com.bitcoin_bank.spring.interfaces;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.exception.WalletNotFoundException;
import io.block.api.utils.BlockIOException;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionManager {
    BankMessage verifyFundsSending(String senderAddress, String receiverAddress, String transactionAmount);
    BankMessage sendRequest(String walletAddress, BigDecimal requestAmount, String email);

    List<BankMessage.ReceivedTransactionMsg> getReceivedTransactionsHistory(String receiverAddress) throws BlockIOException;

    List<BankMessage.SentTransactionMsg> getTransactionsSentHistory(String senderAddress) throws WalletNotFoundException;

    List<BankMessage> getAllTransactions(String address) throws WalletNotFoundException;
}
