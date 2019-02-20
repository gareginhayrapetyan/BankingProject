package com.bitcoin_bank.spring.interfaces;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.exception.UserNotFoundException;
import com.bitcoin_bank.spring.exception.WalletNotFoundException;
import io.block.api.utils.BlockIOException;

public interface IUserManager {
    BankMessage verifyRegistration(BankMessage msg);
    BankMessage verifyUserLogin(BankMessage msg);
    User getUser(String username) throws UserNotFoundException;
    User validateUser(String username, String password) throws UserNotFoundException;
    void generateNewWalletAddressForUser(User user, String label) throws BlockIOException;
}
