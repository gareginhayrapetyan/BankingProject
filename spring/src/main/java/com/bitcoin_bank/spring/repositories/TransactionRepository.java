package com.bitcoin_bank.spring.repositories;

import com.bitcoin_bank.spring.entities.Transaction;
import com.bitcoin_bank.spring.entities.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> getBySenderWallet(Wallet senderWallet);

    List<Transaction> getByReceiverWallet(Wallet receiverWallet);

    List<Transaction> getBySenderWalletOrReceiverWallet(Wallet senderWallet, Wallet receiverWallet);
}
