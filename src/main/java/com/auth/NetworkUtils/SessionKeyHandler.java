package com.auth.NetworkUtils;

import com.auth.Wrapper.ConvertUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class SessionKeyHandler extends AbstractHandler {
    public BigInteger p;
    public BigInteger g;
    private BigInteger mySecret;
    public BigInteger sharedSecret;

    private boolean negotiateCall1() {
        try {
            JSONObject response = HttpUtil.sendPostRequest("/negotiate_key1/", new JSONObject("{}"));
            if (response == null) return false;
            JSONObject responseData = response.getJSONObject("data");
            this.p = new BigInteger(responseData.getString("p"), 16);
            this.g = new BigInteger(responseData.getString("g"), 16);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    private boolean negotiateCall2() {
        String message = g.modPow(mySecret, p).toString(16);
        message = ConvertUtils.zeroRPad(message, 64);
        try {
            JSONObject requset = new JSONObject();
            requset.put("data", message);
            JSONObject response = HttpUtil.sendPostRequest("/negotiate_key2/", requset);
            if(response == null) return false;
            sharedSecret = (new BigInteger(response.getString("data"), 16)).modPow(mySecret, p);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    SessionKeyHandler() {
        logger = LoggerFactory.getLogger(SessionKeyHandler.class);

        if (!negotiateCall1()) {
            logger.error("Negotiate DH key failed at step 1.");
            return;
        }
        this.mySecret = new BigInteger(256, new Random());
        if (!negotiateCall2()) {
            logger.error("Negotiate DH key failed at step 2.");
            return;
        }
        this.completeStatus = true;
    }

    public byte[] getSM4Key() {
        byte[] bintArray = sharedSecret.toByteArray();
        if (bintArray[0] == 0) {
            bintArray = Arrays.copyOfRange(bintArray, 1, bintArray.length);
        }
        return ConvertUtils.zeroRPad(bintArray, 16);
    }
}
