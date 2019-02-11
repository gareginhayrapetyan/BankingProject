package banking.client;

public enum AccountType {
    SAVINGS(7.5),
    CURRENT(4.5);

    private double interestRateValue;

    AccountType(double interestRateValue) {
        this.interestRateValue = interestRateValue;
    }

    public double getInterestRateValue() {
        return interestRateValue;
    }
}
