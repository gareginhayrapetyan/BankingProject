package banking.client;

import banking.interfaces.IAccount;
import banking.interfaces.ICustomer;

public class Account implements IAccount {
    private String accountID;
    private double balance;
    private ICustomer accountOwner;
    private double interestRate;

    public Account(String accountID, ICustomer customer, double interestRate) {
        this.accountID = accountID;
        this.balance = 0;
        this.interestRate = interestRate;
        this.accountOwner = customer;
        accountOwner.addAccount(this);
    }

    public String getAccountID() {
        return this.accountID;
    }

    public double getAccountBalance() {
        return this.balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount < balance) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("not enough funds");
        }
    }

    public void addInterest() {
        this.balance += balance * interestRate;
    }
}
