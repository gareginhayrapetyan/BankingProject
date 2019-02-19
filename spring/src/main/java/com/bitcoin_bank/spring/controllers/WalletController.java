package com.bitcoin_bank.spring.controllers;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.exception.UserNotFoundException;
import com.bitcoin_bank.spring.interfaces.IUserManager;
import io.block.api.utils.BlockIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@Controller
@RequestMapping(path = "/")
public class WalletController {
    @Autowired
    IUserManager userManager;

    @PostMapping(path = "/createwallet")
    public @ResponseBody
    String registerNewUser(@RequestParam String username,
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
}
