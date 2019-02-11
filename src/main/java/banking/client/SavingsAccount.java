package banking.client;

import banking.interfaces.ICustomer;

public class SavingsAccount extends Account {

    public SavingsAccount(String accountID, ICustomer customer, double annualInterestRate) {
        super(accountID, customer, annualInterestRate);
    }
}
