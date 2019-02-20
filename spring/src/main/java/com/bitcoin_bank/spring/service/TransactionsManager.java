package com.bitcoin_bank.spring.service;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.exception.InsufficientFundsException;
import com.bitcoin_bank.spring.exception.WalletNotFoundException;
import com.bitcoin_bank.spring.interfaces.ITransactionManager;
import com.bitcoin_bank.spring.repositories.WalletRepository;
import com.bitcoin_bank.util.Util;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionsManager implements ITransactionManager {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionsManager.class);

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    BitcoinSenderServiceImpl bitcoinSenderService;

    @Autowired
    EmailServiceImpl emailService;

    @Override
    public BankMessage verifyFundsSending(String senderAddress, String receiverAddress, String transactionAmount) {
        try {
            Wallet senderWallet = getWalletByAddress(senderAddress);
            Wallet receiverWallet = getWalletByAddress(receiverAddress);
            BigDecimal amount = new BigDecimal(transactionAmount);
            if (isFundsSufficient(senderWallet, amount)) {
                bitcoinSenderService.withdrawFromWalletToWallet(senderAddress, receiverAddress, amount);
                return Util.confirmationMessage("Success");
            } else {
                return Util.failureMessage("InsufficientFunds in wallet: " + senderAddress);
            }
        } catch (WalletNotFoundException e) {
            return Util.failureMessage("Failure: Check addresses");
        }
    }

    public BankMessage sendRequest(String walletAddress, BigDecimal requestAmount, String email) {

        final String QR_CODE_IMAGE_PATH = "qrImage.png";
        try {
            Util.generateQRCodeImage(walletAddress, 350, 350, QR_CODE_IMAGE_PATH);
            boolean isSentSuccessfully = emailService.sendMessageWithAttachment(email, "Request",
                    "Please send " + requestAmount + " BTC to the address: ", QR_CODE_IMAGE_PATH);
            if (isSentSuccessfully) {
                return Util.confirmationMessage("Request was sent successfully.");
            } else {
                return Util.failureMessage("Failure while sending request message.");
            }
        } catch (WriterException | IOException e) {
            LOG.error("Error while generating qr image: " + e.getMessage());
            return Util.failureMessage("Error while generating qr image");
        }
    }

    public Wallet getWalletByAddress(String address) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.getWalletByAddress(address);

        if (wallet.isPresent()) {
            return wallet.get();
        } else {
            throw new WalletNotFoundException("invalid address");
        }
    }

    boolean isFundsSufficient(Wallet senderWallet, BigDecimal amount) {
        if (senderWallet.getCurrentBalance().compareTo(amount) < 0) {
            return false;
        } else {
            return true;
        }

    }
}
