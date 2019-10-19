package com.auth.Wrapper;

public class ConvertUtils {
    public static String zeroRPad(String src, Integer length) {
        assert src.length() <= length;
        return String.format("%-" + length + "s", src).replace(" ", "0");
    }
}
