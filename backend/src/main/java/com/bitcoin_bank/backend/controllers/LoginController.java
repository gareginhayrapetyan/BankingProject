package com.bitcoin_bank.backend.controllers;

import aca.proto.BankMessage;
import com.bitcoin_bank.backend.interfaces.IUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = "/")
public class LoginController {

    @Autowired
    IUserManager userManager;

    @PostMapping(path = "/login")
    public @ResponseBody
    String registerNewUser(@RequestParam String username, @RequestParam String password) {

        BankMessage loginMsg = BankMessage.newBuilder()
                .setLogin(BankMessage.Login.newBuilder()
                        .setUsername(username)
                        .setPassword(password)
                        .build())
                .build();

        BankMessage response = userManager.verifyUserLogin(loginMsg);
        if (response.hasConfirmation()) {
            return response.getConfirmation().getMessage();
        } else {
            return response.getFailure().getMessage();
        }
    }
}
