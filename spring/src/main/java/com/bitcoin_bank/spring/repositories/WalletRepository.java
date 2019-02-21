package com.bitcoin_bank.spring.repositories;

import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Set<Wallet> getWalletByOwner(User owner);
    Optional<Wallet> getWalletByAddress(String address);
}
