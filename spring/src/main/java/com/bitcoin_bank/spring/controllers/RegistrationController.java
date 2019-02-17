package com.bitcoin_bank.spring.controllers;


import aca.proto.BankMessage;
import com.bitcoin_bank.spring.interfaces.IUserManager;
import com.bitcoin_bank.spring.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/")
public class RegistrationController {
    @Autowired
    IUserManager userManager;

    @GetMapping(path = "/register")
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
            return "Successful registration.";
        } else {
            return "Registration failed.";
        }
    }

}
