package spring.managers;

import aca.proto.BankMessage;

import javafx.util.Pair;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.KeyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.entities.BitcoinWallet;
import spring.entities.User;
import spring.interfaces.IUserManager;
import spring.repositories.UserRepository;
import spring.repositories.WalletRepository;
import spring.utils.Util;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

public class UserManager implements IUserManager {
    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    private PasswordEncoder passwordEncoder;
    private String managerID;


    public UserManager(String managerID) {
        this.managerID = managerID;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Transactional
    @Override
    public Pair<User, BankMessage> verifyRegistration(BankMessage msg) {
        if (msg.hasRegistration()) {
            String firsName = msg.getRegistration().getFirstName();
            String lastName = msg.getRegistration().getLastName();
            String userName = msg.getRegistration().getUsername();
            String email = msg.getRegistration().getEmail();
            String password = msg.getRegistration().getPassword();

            if (!Util.isValidUserName(userName)) {
                return new Pair<>(null, failureMessage("Invalid username"));
            } else if (!Util.isValidEmail(email)) {
                return new Pair<>(null, failureMessage("Invalid email"));
            } else if (userRepository.findByUserName(userName).isPresent()) {
                return new Pair<>(null, failureMessage("Username " + userName + " already exist"));
            } else if (userRepository.findByEmail(email).isPresent()) {
                return new Pair<>(null, failureMessage("Email address " + email + " already in use"));
            } else {
                User newUser = new User(firsName, lastName, userName, email, passwordEncoder.encode(password));
                BitcoinWallet newUsersWallet = createNewWalletForUser(newUser);

                userRepository.save(newUser);
                walletRepository.save(newUsersWallet);


                BankMessage confirmation = BankMessage.newBuilder()
                        .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful registration").build())
                        .build();
                return new Pair<>(newUser, confirmation);
            }
        } else {

            return new Pair<>(null, failureMessage("incorrect message type"));
        }
    }

    @Transactional
    @Override
    public BankMessage verifyUserLogin(BankMessage msg) {
        if (msg.hasLogin()) {
            String username = msg.getLogin().getUsername();
            String password = msg.getLogin().getPassword();

            if (userRepository.findByUserNameAndPassword(username, password).isPresent()) {
                BankMessage confirmation = BankMessage.newBuilder()
                        .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful login").build())
                        .build();
                return confirmation;
            } else {
                return failureMessage("Check username and password.");
            }
        } else {
            return failureMessage("Incorrect message type");
        }
    }

    public BitcoinWallet createNewWalletForUser(User user) {
        final NetworkParameters parameters = TestNet3Params.get();
        Wallet wallet = new Wallet(parameters);

        ECKey key = wallet.currentKey(KeyChain.KeyPurpose.RECEIVE_FUNDS);
        Address address = key.toAddress(parameters);
        System.out.println("Address: " + address);

        BitcoinWallet bitcoinWallet = new BitcoinWallet();
        bitcoinWallet.setCurrentAddress(address.toString());
        bitcoinWallet.setCurrentBalance(BigDecimal.valueOf(wallet.getBalance().value));
        bitcoinWallet.setOwner(user);

        return bitcoinWallet;
    }


    @Transactional
    public User getUser(String username) {
        Optional<User> user = userRepository.findByUserName(username);

        if (user.isPresent()) {
            return user.get();
        } else {
            LOG.error("Not found by username : " + username);
            return null;
        }
    }

    BankMessage failureMessage(String msg) {
        return BankMessage.newBuilder()
                .setFailure(BankMessage.Failure.newBuilder().setMessage(msg).build())
                .build();
    }

}
