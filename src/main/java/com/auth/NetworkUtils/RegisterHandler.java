package com.auth.NetworkUtils;

import com.auth.Wrapper.QRCodeConstant;
import com.auth.Wrapper.QRCodeWrapper;

import java.awt.image.BufferedImage;

public class RegisterHandler extends AbstractHandler{
    private BufferedImage qrcodeImage;

    public BufferedImage getQrcodeImage(){return qrcodeImage;}

    private String username;

    private boolean pcGenerateQRCode(){
        qrcodeImage = QRCodeWrapper.createQRCode(username + QRCodeConstant.RegisterQrCode);
        return qrcodeImage != null;
    }

    public RegisterHandler(String _username_) {
        username = _username_;

        if(!pcGenerateQRCode()) {
            logger.error("Generate qrcode failed!");
            return;
        }
        this.completeStatus = true;
    }
}
