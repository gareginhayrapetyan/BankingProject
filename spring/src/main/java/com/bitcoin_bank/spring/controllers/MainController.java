package com.bitcoin_bank.spring.controllers;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.exception.UserNotFoundException;
import com.bitcoin_bank.spring.interfaces.ITransactionManager;
import com.bitcoin_bank.spring.interfaces.IUserManager;
import com.bitcoin_bank.spring.repositories.UserRepository;
import io.block.api.utils.BlockIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Set;

@Controller
@RequestMapping(path = "/")
public class MainController {
    @Autowired
    IUserManager userManager;

    @Autowired
    ITransactionManager transactionManager;

    @PostMapping(path = "/createwallet")
    public @ResponseBody
    String createNewWallet(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String label) {
        try {
            User user = userManager.validateUser(username, password);
            userManager.generateNewWalletAddressForUser(user, label);
            StringBuilder str = new StringBuilder();
            str.append("your addresses: ");
            Set<Wallet> wallets = user.getWallets();
            wallets.forEach(wallet -> str.append(wallet.getCurrentAddress()));
            return str.toString();
        } catch (UserNotFoundException e) {
            return "Invalid username or password";
        } catch (BlockIOException e) {
            return "Error while generating new address";
        }
    }

    @GetMapping(path = "/send")
    public @ResponseBody
    String send(@RequestParam String senderAddress,
                @RequestParam String receiverAddress,
                @RequestParam String transactionAmount) {

        BankMessage response = transactionManager.verifyFundsSending(senderAddress, receiverAddress, transactionAmount);
        if (response.hasConfirmation()) {
            return response.getConfirmation().getMessage();
        } else if (response.hasFailure()) {
            return response.getFailure().getMessage();
        } else {
            return "";
        }
    }

    @GetMapping(path = "/sendReq")
    public @ResponseBody
    String sendReq(@RequestParam String walletAddress,
                   @RequestParam String requestAmount,
                   @RequestParam String email) {
        BankMessage response = transactionManager.sendRequest(walletAddress, new BigDecimal(requestAmount), email);
        if (response.hasConfirmation()) {
            return response.getConfirmation().getMessage();
        } else if (response.hasFailure()) {
            return response.getFailure().getMessage();
        } else {
            return "";
        }
    }


    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@RequestParam String name, @RequestParam String email) {
        User user = new User();
        user.setUserName(name);
        user.setEmail(email);
        userRepository.save(user);

        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }



}
