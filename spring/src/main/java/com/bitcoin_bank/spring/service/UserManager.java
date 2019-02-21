package com.bitcoin_bank.spring.service;

import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.exception.UserNotFoundException;
import com.bitcoin_bank.spring.interfaces.IUserManager;
import com.bitcoin_bank.spring.repositories.UserRepository;
import com.bitcoin_bank.spring.repositories.WalletRepository;
import com.bitcoin_bank.util.Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.block.api.BlockIO;
import io.block.api.model.AddressByLabel;
import io.block.api.utils.BlockIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import aca.proto.BankMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;


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
    private BlockIO blockIO;


    public UserManager() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("C:\\Users\\Venera\\IdeaProjects\\BankingProjectV1\\spring\\src\\main\\resources\\application.properties");
            properties.load(inputStream);
            this.blockIO = new BlockIO(properties.getProperty("api-key"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public BankMessage verifyRegistration(BankMessage msg) {
        if (msg.hasRegistration()) {
            String firsName = msg.getRegistration().getFirstName();
            String lastName = msg.getRegistration().getLastName();
            String userName = msg.getRegistration().getUsername();
            String email = msg.getRegistration().getEmail();
            boolean isUsing2FA = msg.getRegistration().getIsUsing2FA();

//            String password = msg.getRegistration().getPassword();
            if (!Util.isValidUserName(userName)) {
                return Util.failureMessage("Invalid username");
            } else if (!Util.isValidEmail(email)) {
                return Util.failureMessage("Invalid email");
            } else if (userRepository.findByUserName(userName).isPresent()) {
                return Util.failureMessage("Username " + userName + " already exist");
            } else if (userRepository.findByEmail(email).isPresent()) {
                return Util.failureMessage("Email address " + email + " already in use");
            } else {
                int oneTimePassword = otpService.generateOTP(userName);
                LOG.info("One time password: " + oneTimePassword);
                boolean isMessageSent = emailService.sendSimpleMessage(email, "Registration", "Your one time password is: " + oneTimePassword);

                if (!isMessageSent) {
                    LOG.error("failure while sending email");
                    return Util.failureMessage("Failed");
                } else {
                    User newUser = new User(firsName, lastName, userName, email, passwordEncoder.encode(String.valueOf(oneTimePassword)), isUsing2FA);
                    userRepository.save(newUser);

                    BankMessage confirmation = BankMessage.newBuilder()
                            .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful registration").build())
                            .build();
                    return confirmation;
                }
            }
        } else {
            return Util.failureMessage("incorrect message type");
        }
    }

    @Transactional
    @Override
    public BankMessage verifyUserLogin(BankMessage msg) {
        if (msg.hasLogin()) {
            String username = msg.getLogin().getUsername();

            try {
                User user = getUser(username);
                if (passwordEncoder.matches(msg.getLogin().getPassword(), user.getPassword())) {
                    BankMessage confirmation = BankMessage.newBuilder()
                            .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage("Successful login").build())
                            .build();
                    return confirmation;
                } else {
                    return Util.failureMessage("Invalid password.");
                }
            } catch (UserNotFoundException e) {
                return Util.failureMessage("Invalid username");
            }
        } else {
            return Util.failureMessage("Incorrect message type");
        }
    }

    @Transactional
    public User getUser(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);

        if (user.isPresent()) {
            return user.get();
        } else {
            LOG.error("Not found by username : " + username);
            throw new UserNotFoundException("User not found username: " + username);
        }
    }

    public User validateUser(String username, String password) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return user.get();
            } else {
                LOG.error("Invalid password");
                throw new UserNotFoundException("Invalid password");
            }
        } else {
            LOG.error("Not found by username : " + username);
            throw new UserNotFoundException("User not found username: " + username);
        }
    }


    void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public void generateNewWalletAddressForUser(User user, String label) throws BlockIOException {
//        NewAddress address = blockIO.getNewAddress(label);
        AddressByLabel address = blockIO.getAddressByLabel(label);
//        Wallet wallet = new Wallet();
//        wallet.setCurrentAddress(address.address);
//        wallet.setCurrentBalance(new BigDecimal(0));
//        wallet.setOwner(user);
//        walletRepository.save(wallet);
    }


}
