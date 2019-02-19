package com.bitcoin_bank.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {
    @RequestMapping("/register")
    public String registerPage() {
        return "register";
    }

}
