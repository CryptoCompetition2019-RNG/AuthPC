package com.auth.NetworkUtils;

import com.auth.Wrapper.QRCodeWrapper;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class PcAuthHandlerTest {
    @Test
    public void createPcAuthHandlerTest(){
        String username = "shesl-meow";
        String password = "shesl-meow";

        PcAuthHandler pcAuthHandler = new PcAuthHandler(username, password);
        BufferedImage bufferedImage = pcAuthHandler.getQrcodeImage();
        assertNotNull(bufferedImage);
        // 应该在界面上展示这个二维码

        String qrMessage = QRCodeWrapper.parseQRCode(bufferedImage);
        assertNotNull(qrMessage);
        assertEquals(qrMessage.length(), 128);
        assertEquals(qrMessage.substring(0, 64), username);
        String randomValue = qrMessage.substring(64);

        // 忙等待。用户应该在一分钟内扫描二维码，否则回抛出 assert 异常
        while(pcAuthHandler.checkStatus());
    }

    private void pretentMobile(String username, String randomValue){
        byte[] userSalt = new byte[0];
        try {
            userSalt = Hex.decodeHex("9279f34f1cee0d2dc11ed5916d632da7");
        } catch (DecoderException de){
            fail();
        }

        SessionKeyHandler sessionKeyHandler = new SessionKeyHandler();
    }
}