package banking;

import aca.proto.BankMessage;
import banking.client.AccountType;
import banking.client.User;
import banking.database.DatabaseOperations;
import banking.exceptions.EmailExistsException;
import banking.exceptions.UserNameExistException;
import banking.exceptions.UserNotFoundException;
import banking.exceptions.WrongPasswordException;
import banking.interfaces.IAccount;
import banking.interfaces.ICustomerServiceManager;
import banking.interfaces.IUser;
import javafx.util.Pair;
import org.bitcoinj.core.*;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.KeyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerServiceManager implements ICustomerServiceManager {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceManager.class);
    String managerID;
    List<IUser> users;
    DatabaseOperations databaseOperations;
    PasswordEncoder passwordEncoder;

    public CustomerServiceManager(String managerID) {
        this.managerID = managerID;
        this.users = new ArrayList<>();
        this.databaseOperations = new DatabaseOperations();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public CustomerServiceManager(String managerID, List<IUser> users) {
        this.managerID = managerID;
        if (users != null) {
            this.users = users;
        } else {
            this.users = new ArrayList<>();
        }
    }

    public String getManagerID() {
        return this.managerID;
    }

    public void addCustomer(IUser user) {
        users.add(user);
    }


    public Pair<IUser, BankMessage> verifyRegistration(BankMessage msg) {
        if (msg.hasRegistration()) {
            String firsName = msg.getRegistration().getFirstName(); // verify functions must be added
            String lastName = msg.getRegistration().getLastName();
            String userName = msg.getRegistration().getUsername();
            String email = msg.getRegistration().getEmail();
            String password = msg.getRegistration().getPassword();

            try {
                if (!isValidUserName(userName)) {
                    return new Pair<>(null, failureMessage("Invalid username"));
                }
            } catch (UserNameExistException e) {
                return new Pair<>(null, failureMessage(e.getMessage()));
            }

            try {
                if (!isValidEmail(email)) {
                    return new Pair<>(null, failureMessage("Invalid email"));
                }
            } catch (EmailExistsException e) {
                return new Pair<>(null, failureMessage(e.getMessage()));
            }


            IUser newUser = new User(firsName, lastName, userName, email, passwordEncoder.encode(password));
            users.add(newUser);
            createNewWalletForUser(newUser);

            banking.database.User dbUser = createNewUser(newUser);
            databaseOperations.addUser(dbUser);

            BankMessage confirmation = BankMessage.newBuilder()
                    .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful registration").build())
                    .build();
            return new Pair<>(newUser, confirmation);
        } else {

            return new Pair<>(null, failureMessage("incorrect message type"));
        }
    }

    private boolean isValidUserName(String userName) throws UserNameExistException {
        if (userName.length() < 3) {
            return false;
        }

//        for(IUser user : users) {
//            if(user.getUserName() == userName) {
//                return false;
//            }
//        }

        if (databaseOperations.isUsernameExist(userName)) {
            throw new UserNameExistException("UserName is already in use.");
        }

        return true;
    }


    public boolean isValidEmail(String email) throws EmailExistsException {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
//            for(IUser user : users) {
//                if(user.getEmail() == email) {
//                    throw new EmailExistsException("There is already exist User with that email.");
//                }
//            }
            if (databaseOperations.isEmailExist(email)) {
                throw new EmailExistsException("There is already exist User with that email.");
            }
            return true;
        } else {
            return false;
        }
    }


    banking.database.User createNewUser(IUser user) {
        banking.database.User dbUser = new banking.database.User();
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setUserName(user.getUserName());
        dbUser.setEmail(user.getEmail());
        dbUser.setPassword(user.getPassword());
        dbUser.setCurrentAddress(user.getCurrentAddress().toString());

        return dbUser;
    }


    public BankMessage verifyUserLogin(BankMessage msg) {
        if (msg.hasLogin()) {
            String username = msg.getLogin().getUsername();
            String password = msg.getLogin().getPassword();
//            for(IUser user : users) {
//                if(user.getUserName() == username && user.getPassword() == password) {
//                    BankMessage confirmation = BankMessage.newBuilder()
//                            .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful login").build())
//                            .build();
//                    return confirmation;
//                }
//            }

            try {
                banking.database.User user = databaseOperations.findUser(username, password);

                BankMessage confirmation = BankMessage.newBuilder()
                        .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful login").build())
                        .build();
                return confirmation;

            } catch (UserNotFoundException | WrongPasswordException e) {
                return failureMessage(e.getMessage() + " Please check username and password");
            }
        } else {
            return failureMessage("Incorrect message type");
        }
    }

    BankMessage failureMessage(String msg) {
        return BankMessage.newBuilder()
                .setFailure(BankMessage.Failure.newBuilder().setMessage(msg).build())
                .build();
    }


    public void createNewWalletForUser(IUser user) {
        final NetworkParameters parameters = TestNet3Params.get();
        Wallet wallet = null;
        final File walletFile = new File("test.wallet");

        try {
            wallet = new Wallet(parameters);
            wallet.saveToFile(walletFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ECKey key = wallet.currentKey(KeyChain.KeyPurpose.RECEIVE_FUNDS);
        Address address = key.toAddress(parameters);
        System.out.println("Address: " + address);
//        user.addWallet(wallet);
        user.setWallet(wallet);
        user.setAddress(address);
    }

    public void confirmFundsTransfer(IUser user, IAccount accountFromTransfer, IAccount accountToTransfer) {
        if (users.contains(user)) {
            //todo
        } else {
            throw new RuntimeException();
        }
    }

    public void addAccountToCustomer(IUser user, AccountType accountType) {
        if (!users.contains(user)) {
            throw new RuntimeException();//todo
        }
        // String id


//        IAccount newAccount = new Account("xxxx", user, accountType.getInterestRateValue());
//        customer.addAccount(newAccount);
    }
}
