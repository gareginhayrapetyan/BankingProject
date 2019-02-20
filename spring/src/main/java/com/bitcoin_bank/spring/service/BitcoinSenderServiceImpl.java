package com.bitcoin_bank.spring.service;

import aca.proto.BankMessage;
import com.bitcoin_bank.spring.interfaces.BitcoinSenderService;
import io.block.api.BlockIO;
import io.block.api.model.Withdrawal;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Properties;

@Service
public class BitcoinSenderServiceImpl implements BitcoinSenderService {
    private BlockIO blockIO;
    private Properties properties;

    public BitcoinSenderServiceImpl() {
        properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("C:\\Users\\Venera\\IdeaProjects\\BankingProjectV1\\spring\\src\\main\\resources\\application.properties");
            properties.load(inputStream);
            this.blockIO = new BlockIO(properties.getProperty("api-key"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BankMessage withdrawFromWalletToWallet(String fromAddress, String toAddress, BigDecimal amount) {
        HashMap<String, Double> targetAndAmount = new HashMap<String, Double>(1);
        targetAndAmount.put(toAddress, amount.doubleValue());
        String[] source = {fromAddress};
//        Withdrawal withdrawal = blockIO.withdraw(source, null, targetAndAmount, BlockIO.ParamType.ADDRS, properties.getProperty("secret-pin"));
//        BankMessage withdrawalMsg = BankMessage.newBuilder()
//                .setWithdrawalMsg(BankMessage.WithdrawalMsg.newBuilder()
//                        .setAmountWithdrawn(withdrawal.amountWithdrawn)
//                        .setAmountSent(withdrawal.amountSent)
//                        .setNetworkFee(withdrawal.networkFee)
//                        .setBlockIOFee(withdrawal.blockIOFee)
//                        .setTransactionID(withdrawal.txid).build())
//                .build();
//        return withdrawalMsg;
        //todo
        return null;
    }
}
