package com.bitcoin_bank.util;

import aca.proto.BankMessage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static boolean isValidUserName(String userName) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$");
        Matcher matcher = pattern.matcher(userName);

        return matcher.matches();
    }


    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static int generateOTP(String key) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return otp;
    }


    public static BankMessage failureMessage(String msg) {
        return BankMessage.newBuilder()
                .setFailure(BankMessage.Failure.newBuilder().setMessage(msg).build())
                .build();
    }

    public static BankMessage confirmationMessage(String msg) {
        return BankMessage.newBuilder()
                .setConfirmation(BankMessage.Confirmation.newBuilder().setMessage(msg).build())
                .build();
    }

    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);


        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

}