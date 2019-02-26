package com.bitcoin_bank.backend.service;

import aca.proto.BankMessage;
import com.bitcoin_bank.backend.exception.WalletNotFoundException;
import com.bitcoin_bank.backend.interfaces.BitcoinSenderService;
import com.bitcoin_bank.util.Util;
import io.block.api.BlockIO;
import io.block.api.model.Withdrawal;
import io.block.api.utils.BlockIOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class BitcoinSenderServiceImpl implements BitcoinSenderService {

//    @Autowired
//    private Environment environment;

    @Autowired
    private TransactionsManager transactionsManager;

    //    @Value("${api-key}")
    private String apiKey;
    //
    @Value("${secret-pin}")
    private String secretPin;

    private BlockIO blockIO;


    @Autowired
    public BitcoinSenderServiceImpl(@Value("${api-key}") String apiKey) {
//        String apiKey = environment.getProperty("api-key");
        this.blockIO = new BlockIO(apiKey);
    }

    @Override
    public BankMessage withdrawFromWalletToWallet(String fromAddress, String toAddress, BigDecimal amount) {
        try {
            transactionsManager.getWalletByAddress(fromAddress);
        } catch (WalletNotFoundException e) {
            return Util.failureMessage("Invalid address : " + e.getMessage());
        }

        HashMap<String, Double> targetAndAmount = new HashMap<String, Double>(1);
        targetAndAmount.put(toAddress, amount.doubleValue());
        String[] source = {fromAddress};
        try {
            if (secretPin == null) {
                return Util.failureMessage("Secret pin must be specified");
            }
            Withdrawal withdrawal = blockIO.withdraw(source, BlockIO.ParamType.ADDRS, targetAndAmount, BlockIO.ParamType.ADDRS, secretPin);

            BankMessage withdrawalMsg = BankMessage.newBuilder()
                    .setWithdrawalMsg(BankMessage.WithdrawalMsg.newBuilder()
                            .setTime(System.currentTimeMillis())
                            .setSenderAddress(fromAddress)
                            .setReceiverAddress(toAddress)
                            .setAmountWithdrawn(withdrawal.amountWithdrawn)
                            .setAmountSent(withdrawal.amountSent)
                            .setNetworkFee(withdrawal.networkFee)
                            .setBlockIOFee(withdrawal.blockIOFee)
                            .setTransactionID(withdrawal.txid).build())
                    .build();

            return withdrawalMsg;
        } catch (BlockIOException e) {
            return Util.failureMessage("Transaction failed." + e.getMessage());
        }
    }
}
