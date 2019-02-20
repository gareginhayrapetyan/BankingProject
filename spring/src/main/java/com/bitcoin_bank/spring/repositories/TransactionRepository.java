package com.bitcoin_bank.spring.repositories;

import com.bitcoin_bank.spring.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
