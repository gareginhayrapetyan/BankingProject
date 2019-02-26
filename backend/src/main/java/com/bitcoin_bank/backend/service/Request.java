package com.bitcoin_bank.backend.service;

//import com.bitcoin_bank.spring.interfaces.IRequestManager;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//@Service
//public class Request implements IRequestManager {
//    private static final Logger LOG = LoggerFactory.getLogger(UserManager.class);
//
//    private static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//
//        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
//        byte[] pngData = pngOutputStream.toByteArray();
//        return pngData;
//    }
//
//    private static void sendtoEmail(byte[] qr) {
//    }
//
//
//    @Override
//    public void sendRequest(String address) {
//        try {
//            byte[] qr = generateQRCodeImage(address, 350, 350);
//            sendtoEmail(qr);
//        } catch (WriterException e) {
//            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//        } catch (IOException e) {
//            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//        }
//    }
//}
