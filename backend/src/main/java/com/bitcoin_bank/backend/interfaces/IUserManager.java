package com.bitcoin_bank.backend.interfaces;

import aca.proto.BankMessage;
import com.bitcoin_bank.backend.entities.User;
import com.bitcoin_bank.backend.exception.UserNotFoundException;
import io.block.api.utils.BlockIOException;

public interface IUserManager {
    BankMessage verifyRegistration(BankMessage msg);

    BankMessage verifyUserLogin(BankMessage msg);

    User getUser(String username) throws UserNotFoundException;

    User validateUser(String username, String password) throws UserNotFoundException;

    void generateNewWalletAddressForUser(User user, String label) throws BlockIOException;
}
