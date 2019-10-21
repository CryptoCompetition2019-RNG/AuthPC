package com.auth.NetworkUtils;

import com.auth.Wrapper.QRCodeWrapper;
import com.auth.Wrapper.SM4Wrapper;
import com.auth.Wrapper.ThreadWrapper;
import org.json.JSONObject;

import java.awt.image.BufferedImage;

public class DynamicAuthHandler extends AbstractHandler {
    private String username;
    private String qrMessage;
    private BufferedImage qrcodeImage;

    private SM4Wrapper sm4Wrapper = new SM4Wrapper();
    private SessionKeyHandler sessionKeyHandler;

    public BufferedImage getQrcodeImage() { return qrcodeImage; }

    private boolean dynamicAuthCall1(){
        byte[] sm4_id = sm4Wrapper.encrypt(sessionKeyHandler.getSM4Key().getBytes(), username.getBytes());

        try {
            JSONObject request = new JSONObject();
            request.put("data", sm4_id);
            JSONObject repsonse = HttpUtil.sendPostRequest("/dynamicauth_api1/", request);
            if (repsonse == null){
                logger.error("Network error!");
                return false;
            }
            qrMessage = repsonse.getString("data");
            return true;
        } catch (Exception e){
            logger.error(e.toString());
            return false;
        }
    }

    private boolean dynamicAuthGenerateQRCode(){
        qrcodeImage = QRCodeWrapper.createQRCode(qrMessage);
        return qrcodeImage != null;
    }

    private boolean dynamicAuthCall3(){
        JSONObject emptyRequest = new JSONObject();
        JSONObject response = HttpUtil.sendPostRequest("/dynamicauth_api3/", emptyRequest);
        return (response != null) && (response.getInt("code") == 0);
    }

    public DynamicAuthHandler(String _username_){
        username = _username_;

        sessionKeyHandler = new SessionKeyHandler();
        if(!sessionKeyHandler.checkStatus()){
            logger.error("Failed when negotiate session key.");
            return;
        }

        if(!dynamicAuthCall1()){
            logger.error("Failed when dynamic auth call step 1");
            return;
        }
        if(!dynamicAuthGenerateQRCode()){
            logger.error("Failed when generate QRCode.");
            return;
        }
        ThreadWrapper.setTimeoutAync(() ->{
            for (int tryTimes = 0; !this.dynamicAuthCall3(); tryTimes++) {
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
        }, 0);
    }
}
