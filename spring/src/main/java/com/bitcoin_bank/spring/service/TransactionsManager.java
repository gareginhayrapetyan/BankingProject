package com.bitcoin_bank.spring.service;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.entities.Transaction;
import com.bitcoin_bank.spring.entities.Wallet;
import com.bitcoin_bank.spring.exception.WalletNotFoundException;
import com.bitcoin_bank.spring.interfaces.ITransactionManager;
import com.bitcoin_bank.spring.repositories.TransactionRepository;
import com.bitcoin_bank.spring.repositories.WalletRepository;
import com.bitcoin_bank.util.Util;
import com.google.zxing.WriterException;
import io.block.api.BlockIO;
import io.block.api.model.TransactionReceived;
import io.block.api.model.TransactionsReceived;
import io.block.api.utils.BlockIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service
public final class TransactionsManager implements ITransactionManager {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionsManager.class);

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BitcoinSenderServiceImpl bitcoinSenderService;

    @Autowired
    EmailServiceImpl emailService;
    private BlockIO blockIO;

    public TransactionsManager() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("C:\\Users\\Venera\\IdeaProjects\\BankingProjectV1\\spring\\src\\main\\resources\\application.properties");
            properties.load(inputStream);
            this.blockIO = new BlockIO(properties.getProperty("api-key"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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

    public List<BankMessage.ReceivedTransactionMsg> getReceivedTransactionsHistory(String receiverAddress) throws BlockIOException {
        String[] addresses = {receiverAddress};
        List<BankMessage.ReceivedTransactionMsg> receivedTransactionMsgs = new ArrayList<>();

        TransactionsReceived transactionsReceived = blockIO.getTransactionsReceivedByAddress(addresses, null);
        List<TransactionReceived> transactionReceivedList = transactionsReceived.txs;
        for (TransactionReceived transactionReceived : transactionReceivedList) {
            BankMessage.ReceivedTransactionMsg msg = BankMessage.ReceivedTransactionMsg.newBuilder()
                    .setTransactionID(transactionReceived.txid).setTime(transactionReceived.time)
                    .setAmount(transactionReceived.amountsReceived.get(0).amount)
                    .setSenderAddress(transactionReceived.senders.get(0))
                    .build();
            receivedTransactionMsgs.add(msg);
        }

        return receivedTransactionMsgs;
    }

    public List<BankMessage.SentTransactionMsg> getTransactionsSentHistory(String senderAddress) throws WalletNotFoundException {
        Optional<Wallet> senderWallet = walletRepository.getWalletByAddress(senderAddress);
        List<BankMessage.SentTransactionMsg> sentTransactionMsgs = new ArrayList<>();

//
//        Transaction transactionTest = new Transaction();
//        transactionTest.setDate(new Timestamp(System.currentTimeMillis()));
//        transactionTest.setReceiverWallet(senderWallet.get());
//        transactionTest.setSenderWallet(senderWallet.get());
//        transactionTest.setTransactionAmount(new BigDecimal(0.1));
//        transactionRepository.save(transactionTest);

        if (senderWallet.isPresent()) {
            List<Transaction> transactionsSent = transactionRepository.getBySenderWallet(senderWallet.get());
            for (Transaction transaction : transactionsSent) {
                BankMessage.SentTransactionMsg msg = BankMessage.SentTransactionMsg.newBuilder()
                        .setTransactionID(transaction.getId().toString())
                        .setTime(transaction.getDate().getTime())
                        .setAmount(transaction.getTransactionAmount().toString())
                        .setReceiverAddress(transaction.getReceiverWallet().getCurrentAddress()).build();
                sentTransactionMsgs.add(msg);
            }
            return sentTransactionMsgs;
        } else {
            throw new WalletNotFoundException("Invalid address");
        }

    }

    public List<BankMessage> getAllTransactions(String address) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.getWalletByAddress(address);
        List<BankMessage> allTransactionsMsgs = new ArrayList<>();

        if (wallet.isPresent()) {
            List<Transaction> allTransactions = transactionRepository.getBySenderWalletOrReceiverWallet(wallet.get(), wallet.get());
            for (Transaction transaction : allTransactions) {
                BankMessage msg = null;
                if (transaction.getSenderWallet().equals(wallet.get())) {
                    msg = BankMessage.newBuilder().setSentTransactionMsg(BankMessage.SentTransactionMsg.newBuilder()
                            .setTransactionID(transaction.getId().toString())
                            .setTime(transaction.getDate().getTime())
                            .setAmount(transaction.getTransactionAmount().toString())
                            .setReceiverAddress(transaction.getReceiverWallet().getCurrentAddress()).build())
                            .build();
                } else {
                    msg = BankMessage.newBuilder().setReceivedTransactionMsg(BankMessage.ReceivedTransactionMsg.newBuilder()
                            .setTransactionID(transaction.getId().toString())
                            .setTime(transaction.getDate().getTime())
                            .setAmount(transaction.getTransactionAmount().toString())
                            .setSenderAddress(transaction.getReceiverWallet().getCurrentAddress()).build())
                            .build();
                }
                allTransactionsMsgs.add(msg);
            }
            return allTransactionsMsgs;
        } else {
            throw new WalletNotFoundException("Invalid address.");
        }
    }

    private Wallet getWalletByAddress(String address) throws WalletNotFoundException {
        Optional<Wallet> wallet = walletRepository.getWalletByAddress(address);

        if (wallet.isPresent()) {
            return wallet.get();
        } else {
            throw new WalletNotFoundException("invalid address");
        }
    }

    private boolean isFundsSufficient(Wallet senderWallet, BigDecimal amount) {
        if (senderWallet.getCurrentBalance().compareTo(amount) < 0) {
            return false;
        } else {
            return true;
        }
    }
}
