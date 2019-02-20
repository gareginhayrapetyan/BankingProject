package com.bitcoin_bank.spring.repositories;

import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserNameAndPassword(String userName, String password);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);
}
