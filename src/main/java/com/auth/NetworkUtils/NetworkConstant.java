package com.auth.NetworkUtils;

public interface NetworkConstant {
    String serverUrl = "http://127.0.0.1:8000";

    Integer connectTimeout = 20000;

    Integer readTimeout = 20000;

    int retryInterval = 2000;
}
