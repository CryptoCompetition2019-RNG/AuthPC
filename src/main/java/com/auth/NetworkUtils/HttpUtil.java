package com.auth.NetworkUtils;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final String serverUrl = "http://127.0.0.1:8000";

    public static JSONObject sendPostRequest(String requestRoute, JSONObject requestJson) {
        String requestUrl = serverUrl + requestRoute;
        String requestData = requestJson.toString();
        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.getOutputStream().write(requestData.getBytes());
            connection.getOutputStream().flush();

            if(connection.getResponseCode() != 200)
            {
                logger.error(String.format("Http request %s failed!", serverUrl));
                return null;
            }

            InputStreamReader responseBuffer = new InputStreamReader(connection.getInputStream());
            return new JSONObject(responseBuffer.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
