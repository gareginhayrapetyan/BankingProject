package com.bitcoin_bank.spring.interfaces;

import aca.proto.BankMessage;

public interface IUserManager {
    BankMessage verifyRegistration(BankMessage msg);

    BankMessage verifyUserLogin(BankMessage msg);
}
