package banking.interfaces;

import aca.proto.BankMessage;
import javafx.util.Pair;

public interface ICustomerServiceManager {
    Pair<IUser, BankMessage> verifyRegistration(BankMessage msg);

    BankMessage verifyUserLogin(BankMessage msg);
}
