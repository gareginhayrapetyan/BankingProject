package com.bitcoin_bank.spring.controllers;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.entities.User;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.exception.UserNotFoundException;
import com.bitcoin_bank.spring.exception.WalletNotFoundException;
import com.bitcoin_bank.spring.interfaces.ITransactionManager;
import com.bitcoin_bank.spring.interfaces.IUserManager;
import io.block.api.utils.BlockIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "/")
public class MainController {
    @Autowired
    IUserManager userManager;

    @Autowired
    ITransactionManager transactionsManager;

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
            wallets.forEach(wallet -> str.append(wallet.getCurrentAddress()).append("\n"));
            return str.toString();
        } catch (UserNotFoundException e) {
            return "Invalid username or password";
        } catch (BlockIOException e) {
            return "Error while generating new address" + e.getMessage();
        }
    }

    @GetMapping(path = "/send")
    public @ResponseBody
    String send(@RequestParam String senderAddress,
                @RequestParam String receiverAddress,
                @RequestParam String transactionAmount) {
        BankMessage response = transactionsManager.verifyFundsSending(senderAddress, receiverAddress, transactionAmount);
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
        BankMessage response = transactionsManager.sendRequest(walletAddress, new BigDecimal(requestAmount), email);
        if (response.hasConfirmation()) {
            return response.getConfirmation().getMessage();
        } else if (response.hasFailure()) {
            return response.getFailure().getMessage();
        } else {
            return "";
        }
    }

    @GetMapping(path = "/getReceivedTransactionsHistory")
    public @ResponseBody
    String getReceivedTransactionsHistory(@RequestParam String walletAddress) {
        try {
            List<BankMessage.ReceivedTransactionMsg> receivedTransactionMsgs = transactionsManager.getReceivedTransactionsHistory(walletAddress);
            StringBuilder response = new StringBuilder();
            for (BankMessage.ReceivedTransactionMsg msg : receivedTransactionMsgs) {
                DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                Date result = new Date(msg.getTime());
                String timeString = simple.format(result);
                response.append(msg.getTransactionID())
                        .append(": amount: ").append(msg.getAmount())
                        .append(", network fee: ").append(msg.getNetworkFee())
                        .append(", sender: ").append(msg.getSenderAddress())
                        .append(", time: ").append(timeString).append("\n");
            }
            return response.toString();
        } catch (WalletNotFoundException e) {
            return "failed: " + e.getMessage();
        }
    }

    @GetMapping(path = "/getTransactionsSent")
    public @ResponseBody
    String getTransactionsSent(@RequestParam String walletAddress) {
        try {
            List<BankMessage.SentTransactionMsg> sentTransactionMsgs = transactionsManager.getTransactionsSentHistory(walletAddress);
            StringBuilder response = new StringBuilder();
            for (BankMessage.SentTransactionMsg msg : sentTransactionMsgs) {
                DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                Date result = new Date(msg.getTime());
                String timeString = simple.format(result);
                response.append(msg.getTransactionID())
                        .append(": amount: ").append(msg.getAmount())
                        .append(", network fee: ").append(msg.getNetworkFee())
                        .append(", receiver: ").append(msg.getReceiverAddress())
                        .append(", time: ").append(timeString).append("\n");
            }
            return response.toString();
        } catch (WalletNotFoundException e) {
            return "failed " + e.getMessage();
        }
    }

    @GetMapping(path = "/getAllTransactions")
    public @ResponseBody
    String getAllTransactions(@RequestParam String walletAddress) {
        try {
            List<BankMessage> transactionsMsgs = transactionsManager.getAllTransactions(walletAddress);
            StringBuilder response = new StringBuilder();
            for (BankMessage msg : transactionsMsgs) {

                if (msg.hasReceivedTransactionMsg()) {
                    DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                    Date result = new Date(msg.getReceivedTransactionMsg().getTime());
                    String timeString = simple.format(result);
                    response.append(msg.getReceivedTransactionMsg().getTransactionID())
                            .append(". time: ").append(timeString)
                            .append(" amount: +").append(msg.getReceivedTransactionMsg().getAmount())
                            .append(" sender : ").append(msg.getReceivedTransactionMsg().getSenderAddress()).append("\n");
                } else {
                    DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
                    Date result = new Date(msg.getSentTransactionMsg().getTime());
                    String timeString = simple.format(result);
                    response.append(msg.getSentTransactionMsg().getTransactionID())
                            .append(". time: ").append(timeString)
                            .append(" amount: -").append(msg.getSentTransactionMsg().getAmount())
                            .append(" receiver: ").append(msg.getSentTransactionMsg().getReceiverAddress()).append("\n");
                }

            }
            return response.toString();
        } catch (WalletNotFoundException e) {
            return "failed " + e.getMessage();
        }
    }
}
