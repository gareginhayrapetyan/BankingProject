package banking.interfaces;

import banking.client.AccountType;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Wallet;

public interface IUser {
    void login();

    String getFirstName();

    String getLastName();

    String getUserName();

    String getPassword();

    String getEmail();

    //    void addWallet(Wallet wallet);
    void setWallet(Wallet wallet);

    void setAddress(Address address);

    Address getCurrentAddress();

    Wallet getWallet();

    void logout();

    void changePassword();

    void transferFunds(double amount, IAccount accountFrom, IAccount accountTo);

    //    void viewWalletsBalances();
    void viewWalletBalance();

    void addAccount(IAccount account);
}
