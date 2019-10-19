package com.auth.NetworkUtils;

import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class PcAuthHandlerTest {
    @Test
    public void createPcAuthHandlerTest(){
        String username = "shesl-meow";
        String password = "shesl-meow";

        PcAuthHandler pcAuthHandler = new PcAuthHandler(username, password);
        BufferedImage bufferedImage = pcAuthHandler.getQrcodeImage();
        assertNotNull(bufferedImage);
        // 应该在界面上展示这个二维码

        // 忙等待。用户应该在一分钟内扫描二维码，否则回抛出 assert 异常
        while(pcAuthHandler.checkStatus());
    }
}