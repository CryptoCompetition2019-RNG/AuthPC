package com.auth.NetworkUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static CookieManager cookieManager = new CookieManager();

    public static JSONObject sendPostRequest (String requestRoute, JSONObject requestJson) {
        CookieStore cookieStore = cookieManager.getCookieStore();

        String requestUrl = NetworkConstant.serverUrl + requestRoute;
        String requestData = requestJson.toString();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(NetworkConstant.readTimeout);
            connection.setConnectTimeout(NetworkConstant.connectTimeout);

            //            connection.connect();
            List<HttpCookie> cookies = cookieStore.getCookies();
            if (cookies.size() > 0) {
                connection.setRequestProperty("Cookie", StringUtils.join(cookies, ";"));
            }
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestData.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            if (connection.getResponseCode() != 200) {
                logger.error("Http request failed!");
                return null;
            }

            List<String> setCookies = connection.getHeaderFields().get("Set-Cookie");
            if (setCookies != null) {
                for (String setCookie : setCookies) {
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(setCookie).get(0));
                }
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
            return new JSONObject(stringBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
