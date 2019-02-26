package com.bitcoin_bank.backend.interfaces;

public interface EmailService {
    boolean sendSimpleMessage(String to, String subject, String text);

    boolean sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);
}
