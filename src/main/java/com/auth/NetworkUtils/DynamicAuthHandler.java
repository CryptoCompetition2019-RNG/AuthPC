package com.auth.NetworkUtils;

import com.auth.Wrapper.ConvertUtils;
import com.auth.Wrapper.QRCodeConstant;
import com.auth.Wrapper.QRCodeWrapper;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.zz.gmhelper.SM4Util;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

public class DynamicAuthHandler extends AbstractHandler {
    private String username;
    private BufferedImage qrcodeImage;
    private SessionKeyHandler sessionKeyHandler;

    public BufferedImage getQrcodeImage() { return qrcodeImage; }
    public byte[] getSm4SessionKey() { return sessionKeyHandler.getSM4Key(); }

    private boolean dynamicAuthCall1() {
        byte[] sm4SessionKey = sessionKeyHandler.getSM4Key();
        try {
            byte[] cipherRequest = SM4Util.encrypt_Ecb_NoPadding(
                    sm4SessionKey, username.getBytes(StandardCharsets.US_ASCII)
            );
            JSONObject request = new JSONObject() {{
                put("data", Hex.encodeHexString(cipherRequest));
            }};
            JSONObject repsonse = HttpUtil.sendPostRequest("/dynamicauth_api1/", request);
            if ((repsonse == null) || (repsonse.getInt("code") != 0)) return false;
            String qrMessage = username + repsonse.getString("data") + QRCodeConstant.DynamicQrCode;
            qrcodeImage = QRCodeWrapper.createQRCode(qrMessage);
            return qrcodeImage != null;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    private boolean dynamicAuthCall3() {
        JSONObject emptyRequest = new JSONObject();
        JSONObject response = HttpUtil.sendPostRequest("/dynamicauth_api3/", emptyRequest);
        return (response != null) && (response.getInt("code") == 0);
    }

    public DynamicAuthHandler(String _username_) {
        username = ConvertUtils.zeroRPad(_username_, 64);

        sessionKeyHandler = new SessionKeyHandler();
        if (!sessionKeyHandler.checkStatus()) {
            logger.error("Failed when negotiate session key.");
            return;
        }

        if (!dynamicAuthCall1()) {
            logger.error("Failed when dynamic auth call step 1");
            return;
        }
    }

    public boolean checkIfScaned() {
        for (int tryTimes = 0; !this.dynamicAuthCall3(); tryTimes++) {
            if (tryTimes >= 60*1000 / NetworkConstant.retryInterval) { return false; }
            try { Thread.sleep(NetworkConstant.retryInterval); }
            catch (Exception e) { return false; }
        }
        this.completeStatus = true;
        return true;
    }
}
