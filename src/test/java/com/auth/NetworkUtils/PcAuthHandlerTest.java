package com.auth.NetworkUtils;

import com.auth.Wrapper.ConvertUtils;
import com.auth.Wrapper.QRCodeWrapper;
import com.jd.blockchain.utils.io.BytesUtils;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.junit.Test;
import org.zz.gmhelper.SM4Util;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class PcAuthHandlerTest {
    @Test
    public void createPcAuthHandlerTest(){
        String username = ConvertUtils.zeroRPad( "shesl-meow", 64);
        String password = ConvertUtils.zeroRPad("shesl-meow", 64);

        PcAuthHandler pcAuthHandler = new PcAuthHandler(username, password);
        BufferedImage bufferedImage = pcAuthHandler.getQrcodeImage();
        assertNotNull(bufferedImage);
        // 应该在界面上展示这个二维码
        // 忙等待。用户应该在一分钟内扫描二维码，否则回抛出 assert 异常

        // todo: 模拟用户扫描二维码并且向后端传送数据
        String qrMessage = QRCodeWrapper.parseQRCode(bufferedImage);
        assertNotNull(qrMessage);
        assertEquals(qrMessage.length(), 128);
        assertEquals(qrMessage.substring(0, 64), username);
        String randomValue = qrMessage.substring(64);
        this.pretentMobile(username, randomValue);

        pcAuthHandler.checkerThread.start();
        try {
            pcAuthHandler.checkerThread.join();
        } catch (InterruptedException ie) { fail(); }
        assertTrue(pcAuthHandler.checkStatus());
    }

    private void pretentMobile(String username, String randomValue) {
        try {
            byte[] userSaltKey = Hex.decodeHex("9279f34f1cee0d2dc11ed5916d632da7");
            assertEquals(username.length(), 64);
            assertEquals(randomValue.length(), 64);
            assertEquals(userSaltKey.length, 16);

            SessionKeyHandler sessionKeyHandler = new SessionKeyHandler();
            assertTrue(sessionKeyHandler.checkStatus());
            final byte[] cipher1 = SM4Util.encrypt_Ecb_NoPadding(sessionKeyHandler.getBytesSM4Key(), username.getBytes());
            final byte[] cipher2 = SM4Util.encrypt_Ecb_NoPadding(userSaltKey, randomValue.getBytes());

            JSONObject request = new JSONObject(){{
                put("data", Hex.encodeHexString(BytesUtils.concat(cipher1, cipher2)));
            }};
            JSONObject response = HttpUtil.sendPostRequest("/pcauth_api1/", request);
            assertTrue((response != null) && (response.getInt("code") == 0));
        } catch (Exception e){
            fail();
        }
    }
}