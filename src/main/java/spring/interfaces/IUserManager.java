package spring.interfaces;

import aca.proto.BankMessage;
import javafx.util.Pair;
import spring.entities.User;

public interface IUserManager {
    Pair<User, BankMessage> verifyRegistration(BankMessage msg);

    BankMessage verifyUserLogin(BankMessage msg);
}
