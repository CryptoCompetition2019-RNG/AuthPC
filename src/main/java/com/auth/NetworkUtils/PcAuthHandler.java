package com.auth.NetworkUtils;

import com.auth.Wrapper.*;
import com.jd.blockchain.utils.io.BytesUtils;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.zz.gmhelper.SM3Util;
import org.zz.gmhelper.SM4Util;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class PcAuthHandler extends AbstractHandler {
    private String randomToken;
    private String username;
    private String password;
    private String A_pwd = null;
    private String B_pwd = null;
    private BufferedImage qrcodeImage;

    private SessionKeyHandler sessionKeyHandler;

    public BufferedImage getQrcodeImage() {
        return qrcodeImage;
    }

    private boolean pcAuthGenerateQRCode() {
        qrcodeImage = QRCodeWrapper.createQRCode(username + randomToken + QRCodeConstant.PcQrCode);
        return qrcodeImage != null;
    }

    private boolean pcAuthCall2() {
        byte[] sm4SessionKey = sessionKeyHandler.getBytesSM4Key();
        try {
            byte[] cipherRequest = SM4Util.encrypt_Ecb_NoPadding(sm4SessionKey, username.getBytes());

            System.out.println(String.format("plain: %s", username));
            System.out.println(String.format("key: %s", Hex.encodeHexString(sm4SessionKey)));
            System.out.println(String.format("cipher: %s", Hex.encodeHexString(cipherRequest)));
            JSONObject request = new JSONObject() {{
                put("data", Hex.encodeHexString(cipherRequest));
            }};
            JSONObject response = HttpUtil.sendPostRequest("/pcauth_api2/", request);
            if ((response == null) || (response.getInt("code") != 0)) return false;

            byte[] cipherResponse = Hex.decodeHex(response.getString("data"));
            byte[] plainResponse = SM4Util.decrypt_Ecb_NoPadding(sm4SessionKey, cipherResponse);
            if (plainResponse.length != 128) return false;
            String rToken = new String(Arrays.copyOfRange(plainResponse, 0, 64));
            A_pwd = new String(Arrays.copyOfRange(plainResponse, 64, 128));
            return randomToken.equals(rToken);
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    private void calculateBpwd() {
        BigInteger A = new BigInteger(A_pwd, 16);
        BigInteger hashPassword = new BigInteger(SM3Util.hash(password.getBytes()));
        BigInteger exponent = new BigInteger(
                BytesUtils.concat(A.xor(hashPassword).toByteArray(), hashPassword.toByteArray())
        );

        B_pwd = ConvertUtils.zeroRPad(
                sessionKeyHandler.g.modPow(exponent, sessionKeyHandler.p).toString(16), 64
        );
    }

    private boolean pcAuthCall3() {
        byte[] sm4SessionKey = sessionKeyHandler.getBytesSM4Key();
        try {
            byte[] cipherRequest = SM4Util.encrypt_Ecb_NoPadding(sm4SessionKey, (randomToken + B_pwd).getBytes());

            JSONObject request = new JSONObject() {{
                put("data", Hex.encodeHexString(cipherRequest));
            }};
            JSONObject response = HttpUtil.sendPostRequest("/pcauth_api3/", request);

            return (response != null) && (response.getInt("code") == 0);
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    public PcAuthHandler(String _username_, String _password_) {
        username = ConvertUtils.zeroRPad(_username_, 64);
        password = ConvertUtils.zeroRPad(_password_, 64);
        BigInteger randomBInt = new BigInteger(256, new Random());
        randomToken = ConvertUtils.zeroRPad(randomBInt.toString(16), 64);

        if (!this.pcAuthGenerateQRCode()) {
            logger.error("Generate qrcode failed");
            return;
        }
    }

    public boolean checkIfScaned() {
        sessionKeyHandler = new SessionKeyHandler();
        if (!sessionKeyHandler.checkStatus()) {
            logger.error("Failed when negotiate session key.");
            return false;
        }

        for (int tryTimes = 0; !this.pcAuthCall2(); tryTimes++) {
            if (tryTimes >= 60000 / NetworkConstant.retryInterval) {
                logger.error("Please scan QRCode in 1 min.");
                return false;
            }
            try {
                Thread.sleep(NetworkConstant.retryInterval);
            } catch (InterruptedException ie) {
                logger.info(ie.toString());
            }
        }

        this.calculateBpwd();
        if (!this.pcAuthCall3()) {
            logger.error("Failed when pc auth call step 3");
            return false;
        }
        this.completeStatus = true;
        return true;
    }
}
