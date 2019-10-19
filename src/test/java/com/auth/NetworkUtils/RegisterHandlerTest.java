package com.auth.NetworkUtils;

import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class RegisterHandlerTest {

    @Test
    public void createRegisterHandlerTest() {
        String username = "shesl-meow";
        RegisterHandler registerHandler = new RegisterHandler(username);
        assertTrue(registerHandler.checkStatus());

        BufferedImage qrcodeImage = registerHandler.getQrcodeImage();
        assertNotNull(qrcodeImage);
        // 应该在界面上展示二维码，给手机端扫描
    }
}