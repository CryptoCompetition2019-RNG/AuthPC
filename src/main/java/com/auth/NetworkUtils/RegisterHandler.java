package com.auth.NetworkUtils;

import com.auth.Wrapper.QRCodeWrapper;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

public class RegisterHandler extends AbstractHandler{
    public BufferedImage qrcode;

    private String username;

    private boolean pcRegisterCall(){
        qrcode = QRCodeWrapper.createQRCode(username);
        return qrcode != null;
    }

    public RegisterHandler(String _username_) {
        username = _username_;

        if(!pcRegisterCall()) {
            logger.error("Generate qrcode failed!");
            return;
        }
        this.completeStatus = true;
    }
}
