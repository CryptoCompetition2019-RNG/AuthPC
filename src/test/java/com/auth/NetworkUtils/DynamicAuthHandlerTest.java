package com.auth.NetworkUtils;

import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class DynamicAuthHandlerTest {
    @Test
    public void createDynamicAuthHandlerTest(){
        String username = "shesl-meow";

        DynamicAuthHandler dynamicAuthHandler = new DynamicAuthHandler(username);
        BufferedImage bufferedImage = dynamicAuthHandler.getQrcodeImage();
        assertNotNull(bufferedImage);
        // 应该在界面上展示这个二维码

        // 忙等待。用户应该在一分钟内扫描二维码，否则回抛出 assert 异常
        while(dynamicAuthHandler.checkStatus());
    }
}