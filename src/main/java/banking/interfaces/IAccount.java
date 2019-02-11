package banking.interfaces;

public interface IAccount {
    String getAccountID();

    double getAccountBalance();

    void deposit(double amount);

    void withdraw(double amount);
}
