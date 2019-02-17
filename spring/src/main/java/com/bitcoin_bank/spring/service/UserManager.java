package com.bitcoin_bank.spring.service;

import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.interfaces.IUserManager;
import com.bitcoin_bank.spring.repositories.UserRepository;
import com.bitcoin_bank.spring.repositories.WalletRepository;
import com.bitcoin_bank.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import aca.proto.BankMessage;

import java.util.Optional;


@Service
public class UserManager implements IUserManager {
    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    OtpService otpService;

    private PasswordEncoder passwordEncoder;


    public UserManager() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Override
    public BankMessage verifyRegistration(BankMessage msg) {
        if (msg.hasRegistration()) {
            String firsName = msg.getRegistration().getFirstName();
            String lastName = msg.getRegistration().getLastName();
            String userName = msg.getRegistration().getUsername();
            String email = msg.getRegistration().getEmail();
//            String password = msg.getRegistration().getPassword();

            if (!Util.isValidUserName(userName)) {
                return failureMessage("Invalid username");
            } else if (!Util.isValidEmail(email)) {
                return failureMessage("Invalid email");
            } else if (userRepository.findByUserName(userName).isPresent()) {
                return failureMessage("Username " + userName + " already exist");
            } else if (userRepository.findByEmail(email).isPresent()) {
                return failureMessage("Email address " + email + " already in use");
            } else {
                int oneTimePassword = otpService.generateOTP(userName);
                LOG.info("One time password: " + oneTimePassword);
                boolean isMessageSent = emailService.sendSimpleMessage(email, "Registration", "Your one time password is: " + oneTimePassword);

                if (!isMessageSent) {
                    LOG.error("failure while sending email");
                    return failureMessage("Failed");
                } else {
                    User newUser = new User(firsName, lastName, userName, email, passwordEncoder.encode(String.valueOf(oneTimePassword)));
                    Wallet newUsersWallet = createNewWalletForUser(newUser);

                    userRepository.save(newUser);
//                    walletRepository.save(newUsersWallet);

                    BankMessage confirmation = BankMessage.newBuilder()
                            .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful registration").build())
                            .build();
                    return confirmation;
                }
            }
        } else {
            return failureMessage("incorrect message type");
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

    public Wallet createNewWalletForUser(User user) {
        return null;//todo
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
