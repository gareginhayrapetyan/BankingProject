package com.bitcoin_bank.backend.repositories;

import com.bitcoin_bank.backend.entities.Transaction;
import com.bitcoin_bank.backend.entities.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> getBySenderWallet(Wallet senderWallet);

    List<Transaction> getByReceiverWallet(Wallet receiverWallet);

    List<Transaction> getBySenderWalletOrReceiverWallet(Wallet senderWallet, Wallet receiverWallet);
}
