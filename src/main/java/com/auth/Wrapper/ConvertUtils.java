package com.auth.Wrapper;

public class ConvertUtils {
    public static String zeroRPad(String src, Integer length) {
        if(src.length() > length) {
            src = src.substring(0, length);
        }
        return String.format("%-" + length + "s", src).replace(" ", "0");
    }
}
