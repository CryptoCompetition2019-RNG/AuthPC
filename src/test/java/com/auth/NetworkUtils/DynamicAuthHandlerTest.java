package com.auth.NetworkUtils;

import com.auth.Wrapper.QRCodeConstant;
import com.auth.Wrapper.QRCodeWrapper;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.zz.gmhelper.SM4Util;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicAuthHandlerTest {
    @Test
    public void createDynamicAuthHandlerTest(){
        String username = "shesl-meow";

        DynamicAuthHandler dynamicAuthHandler = new DynamicAuthHandler(username);
        BufferedImage bufferedImage = dynamicAuthHandler.getQrcodeImage();
        assertNotNull(bufferedImage);
        // todo 应该在界面上展示这个二维码;；用户应该在一分钟内扫描二维码

        this.mockMobile(bufferedImage, dynamicAuthHandler.getSm4SessionKey()); // todo: 模拟用户扫描二维码并且向后端传送数据

        assertTrue(dynamicAuthHandler.checkIfScaned());  // todo: PC 端展示了完二维码图片后调用这个方法，不断向后端请求是否扫码完成
        assertTrue(dynamicAuthHandler.checkStatus());
    }

    public void mockMobile(BufferedImage bufferedImage, byte[] sm4SessionKey) {
        String qrMessage = QRCodeWrapper.parseQRCode(bufferedImage);
        assertNotNull(qrMessage);
        assertEquals(qrMessage.length(), 64 + 128 + 1);
        assertEquals(qrMessage.substring(64 + 128), QRCodeConstant.DynamicQrCode);

        try {
            byte[] cipherRandom = Hex.decodeHex(qrMessage.substring(64, 64 + 128));
            byte[] userSaltKey = Hex.decodeHex("9185c9d112d6515a2621c623e9cf2afa");
            byte[] plainiRandom = SM4Util.decrypt_Ecb_NoPadding(userSaltKey, cipherRandom);

            byte[] username = qrMessage.substring(0, 64).getBytes(StandardCharsets.US_ASCII);
            byte[] hashIMEI = "f20e4a1d45bf4935274a262820645479ed7e46dd289e6c6ff04e8aadaad73474".getBytes(StandardCharsets.US_ASCII);

            byte[] plainRequest = ByteUtils.concatenate(ByteUtils.concatenate(username, hashIMEI), plainiRandom);
            byte[] cipherRequest = SM4Util.encrypt_Ecb_NoPadding(sm4SessionKey, plainRequest);
            System.out.println(String.format("plain: %s", Hex.encodeHexString(plainRequest)));
            System.out.println(String.format("key: %s", Hex.encodeHexString(sm4SessionKey)));
            System.out.println(String.format("cipher: %s", Hex.encodeHexString(cipherRequest)));

            JSONObject request = new JSONObject(){{ put("data", Hex.encodeHexString(cipherRequest)); }};
            JSONObject response = HttpUtil.sendPostRequest("/dynamicauth_api2/", request);
            assertTrue((response != null) && (response.getInt("code") == 0));
        } catch (Exception e){ fail(); }
    }
}