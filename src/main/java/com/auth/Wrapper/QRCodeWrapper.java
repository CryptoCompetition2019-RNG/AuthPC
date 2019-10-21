package com.auth.Wrapper;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QRCodeWrapper {
    private static Logger logger = LoggerFactory.getLogger(QRCodeWrapper.class);

    public static BufferedImage createQRCode(String qrMessage){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix;
        try{
            byteMatrix = qrCodeWriter.encode(qrMessage, BarcodeFormat.QR_CODE, QRCodeConstant.width, QRCodeConstant.height);
        } catch (Exception e) {
            logger.error(e.toString());
            return null;
        }

        BufferedImage bufferedImage = new BufferedImage(QRCodeConstant.width, QRCodeConstant.height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics();
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, QRCodeConstant.width, QRCodeConstant.height);
        graphics2D.setColor(Color.BLACK);

        for (int i = 0; i < QRCodeConstant.width; i++) {
            for (int j = 0; j < QRCodeConstant.height; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics2D.fillRect(i, j, 1, 1);
                }
            }
        }
        return bufferedImage;
    }

    public static String parseQRCode(BufferedImage bufferedImage) {
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        HybridBinarizer hybridBinarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
        try{
            Result result = (new MultiFormatReader()).decode(binaryBitmap);
            return result.getText();
        } catch (NotFoundException nfe) {
            return null;
        }
    }
}
