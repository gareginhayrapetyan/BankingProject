package banking.interfaces;

public interface ICustomer {
    String getFirstName();

    String getLastName();

    String getCustomerID();

    void addAccount(IAccount account);
}
