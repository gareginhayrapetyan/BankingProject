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
public class RegistrationController {
    @Autowired
    IUserManager userManager;

    @PostMapping(path = "/register")
    public @ResponseBody
    String registerNewUser(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String username,
                           @RequestParam String email) {

        BankMessage registrationMsg = BankMessage.newBuilder()
                .setRegistration(BankMessage.Registration.newBuilder()
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setUsername(username)
                        .setEmail(email)
                        .setIsUsing2FA(true)
                        .build())
                .build();

        BankMessage response = userManager.verifyRegistration(registrationMsg);
        if (response.hasConfirmation()) {
            return response.getConfirmation().getMessage();
        } else {
            return response.getFailure().getMessage();
        }
    }

}
