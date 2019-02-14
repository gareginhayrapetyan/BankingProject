package banking.client;

import banking.interfaces.IAccount;
import banking.interfaces.IUser;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class User implements IUser {
    private static final Logger LOG = LoggerFactory.getLogger(User.class);
    private String firstName;
    private String lastName;
    private String userName;
    private String customerID;
    private String email;
    private List<IAccount> accounts;
    private String password;
    private Wallet wallet;
    private Address address;

    public User(String firstName, String lastName, String userName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

//    public void addWallet(BitcoinWallet wallet) {
//        wallets.add(wallet);
//    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public Address getCurrentAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void login() {

    }

    public void logout() {

    }

    public void changePassword() {

    }

    public void transferFunds(double amount, IAccount accountFromTransfer, IAccount accountToTransfer) {

    }

//    public void viewWalletsBalances() {
//        for(BitcoinWallet wallet : wallets) {
//            LOG.info("Balance: " + wallet.getBalance().toString());
//        }
//    }

    public void viewWalletBalance() {
        LOG.info("Balance: " + wallet.getBalance().toString());
    }

    public void addAccount(IAccount account) {
        accounts.add(account);
    }
}
