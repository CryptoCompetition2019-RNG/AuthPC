package com.auth.NetworkUtils;

import com.auth.Wrapper.ConvertUtils;
import com.auth.Wrapper.QRCodeConstant;
import com.auth.Wrapper.QRCodeWrapper;
import com.jd.blockchain.utils.io.BytesUtils;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.junit.Test;
import org.zz.gmhelper.SM4Util;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class PcAuthHandlerTest {
    @Test
    public void createPcAuthHandlerTest(){
        String username = ConvertUtils.zeroRPad( "shesl-meow", 64);
        String password = ConvertUtils.zeroRPad("shesl-meow", 64);

        PcAuthHandler pcAuthHandler = new PcAuthHandler(username, password);
        BufferedImage bufferedImage = pcAuthHandler.getQrcodeImage();
        assertNotNull(bufferedImage);
        // todo: 应该在界面上展示这个二维码。用户应该在一分钟内扫描二维码

        this.mockMobile(bufferedImage); // todo: 模拟用户扫描二维码并且向后端传送数据

        assertTrue(pcAuthHandler.checkIfScaned()); // todo: PC 端展示了完二维码图片后调用这个方法，不断向后端请求是否扫码完成
        assertTrue(pcAuthHandler.checkStatus());
    }

    private void mockMobile(BufferedImage bufferedImage) {
        String qrMessage = QRCodeWrapper.parseQRCode(bufferedImage);
        assertNotNull(qrMessage);
        assertEquals(qrMessage.length(), 128 + 1);
        String username = qrMessage.substring(0, 64);
        String randomValue = qrMessage.substring(64, 128);
        assertEquals(qrMessage.substring(128), QRCodeConstant.PcQrCode);
        try {
            SessionKeyHandler sessionKeyHandler = new SessionKeyHandler();
            assertTrue(sessionKeyHandler.checkStatus());

            JSONObject askSaltRequest = new JSONObject(){{ put("data", username); }};
            JSONObject askSaltResponse = HttpUtil.sendPostRequest("/ask_salt/",askSaltRequest);
            assertTrue((askSaltResponse != null) && (askSaltResponse.getInt("code") == 0));
            byte[] userSaltKey = Hex.decodeHex(askSaltResponse.getString("data"));
            assertEquals(username.length(), 64);
            assertEquals(randomValue.length(), 64);
            assertEquals(userSaltKey.length, 16);

            final byte[] cipher1 = SM4Util.encrypt_Ecb_NoPadding(
                    sessionKeyHandler.getSM4Key(), username.getBytes(StandardCharsets.US_ASCII)
            );
            final byte[] cipher2 = SM4Util.encrypt_Ecb_NoPadding(
                    userSaltKey, randomValue.getBytes(StandardCharsets.US_ASCII)
            );
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