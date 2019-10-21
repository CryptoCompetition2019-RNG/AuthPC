package com.auth.NetworkUtils;

import com.auth.Wrapper.*;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.Random;

public class PcAuthHandler extends AbstractHandler {
    private String randomToken;
    private String username;
    private String password;
    private String A_pwd = null;
    private String B_pwd = null;
    private BufferedImage qrcodeImage;

    private SM4Wrapper sm4Wrapper = new SM4Wrapper();
    private SM3Wrapper sm3Wrapper = new SM3Wrapper();
    private SessionKeyHandler sessionKeyHandler;

    public BufferedImage getQrcodeImage(){return qrcodeImage;}

    private boolean pcAuthGenerateQRCode() {
        qrcodeImage = QRCodeWrapper.createQRCode(username + randomToken);
        return qrcodeImage != null;
    }

    private boolean pcAuthCall2(){
        byte[] sm4_id = sm4Wrapper.encrypt(sessionKeyHandler.getSM4Key().getBytes(), username.getBytes());

        byte[] cipherResponse;
        try {
            JSONObject request = new JSONObject();
            request.put("data", new String(sm4_id));
            JSONObject response = HttpUtil.sendPostRequest("/pcauth_api2/", request);
            if (response == null) {
                logger.error("Network error!");
                return false;
            }
            if (response.getInt("code") != 0){
                logger.warn("Haven't log in yet!");
                return false;
            }
            cipherResponse = response.getString("data").getBytes();
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }

        String plainResponse = new String(sm4Wrapper.decrypt(sessionKeyHandler.getSM4Key().getBytes(), cipherResponse));
        if(!plainResponse.substring(0, 64).equals(randomToken)) {
            logger.error("Server random token auth failed!");
            return false;
        }
        A_pwd = plainResponse.substring(64);
        return true;
    }

    private void calculateBpwd(){
        String hashPwd = new String(sm3Wrapper.hash(password.getBytes()));
        String lexp = (new BigInteger(A_pwd, 16)).xor(new BigInteger(hashPwd, 16)).toString(16);
        BigInteger exponent = new BigInteger(lexp + hashPwd, 16);

        B_pwd = ConvertUtils.zeroRPad(sessionKeyHandler.g.modPow(exponent, sessionKeyHandler.p).toString(16), 64);
    }

    private boolean pcAuthCall3(){
        byte[] sm4_rB = sm4Wrapper.encrypt(sessionKeyHandler.getSM4Key().getBytes(), (randomToken + B_pwd).getBytes());

        try {
            JSONObject request = new JSONObject();
            request.put("data", sm4_rB);
            JSONObject response = HttpUtil.sendPostRequest("/pcauth_api3/", request);
            return (response != null) && (response.getInt("code") == 0);
        } catch (Exception e){
            logger.error(e.toString());
            return false;
        }
    }

    public PcAuthHandler(String _username_, String _password_){
        username = ConvertUtils.zeroRPad(_username_, 64);
        password = ConvertUtils.zeroRPad(_password_, 64);
        BigInteger randomBInt = new BigInteger(256, new Random());
        randomToken = ConvertUtils.zeroRPad(randomBInt.toString(16), 64);

        sessionKeyHandler = new SessionKeyHandler();
        if (!sessionKeyHandler.checkStatus()) {
            logger.error("Failed when negotiate session key.");
            return;
        }
        if(!this.pcAuthGenerateQRCode()){
            logger.error("Generate qrcode failed");
            return;
        }

        ThreadWrapper.setTimeoutAync(() -> {
            for (int tryTimes = 0; !this.pcAuthCall2(); tryTimes++) {
                if(tryTimes >= 60/ NetworkConstant.retryInterval){
                    logger.error("Please scan QRCode in 1 min.");
                    assert false;
                }
                try {
                    Thread.sleep(NetworkConstant.retryInterval);
                } catch (Exception e){
                    logger.error(e.toString());
                    return;
                }
            }
            this.calculateBpwd();
            if(!this.pcAuthCall3()){
                logger.error("Failed when pc auth call step 3");
                return;
            }
            this.completeStatus = true;
        }, 0);
    }
}
