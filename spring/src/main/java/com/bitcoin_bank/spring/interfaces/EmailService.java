package com.bitcoin_bank.spring.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    boolean sendSimpleMessage(String to, String subject, String text);

    boolean sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);
}
