package banking.client;

import banking.interfaces.IAccount;
import banking.interfaces.ICustomer;

import java.util.ArrayList;
import java.util.List;

public class Customer implements ICustomer {
    private String firstName;
    private String lastName;
    private String customerID;
    private String email;
    private List<IAccount> accounts;

    public Customer(String firstName, String lastName, String customerID, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.customerID = customerID;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public void addAccount(IAccount account) {
        accounts.add(account);
    }

    public List<IAccount> getAccounts() {
        return accounts;
    }
}
