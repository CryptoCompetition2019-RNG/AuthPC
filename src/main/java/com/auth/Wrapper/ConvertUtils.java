package com.auth.Wrapper;

import java.util.Arrays;

public class ConvertUtils {
    public static String zeroRPad(String src, Integer length) {
        if(src.length() > length) {
            return src.substring(0, length);
        } else {
            return String.format("%-" + length + "s", src).replace(" ", "0");
        }
    }

    public static byte[] zeroRPad(byte[] src, Integer length) {
        if(src.length > length) {
            return Arrays.copyOfRange(src, 0, length);
        } else {
            String srcStr = new String(src);
            String dstStr = String.format("%-" + length + "s", srcStr).replace(" ", "0");
            return dstStr.getBytes();
        }
    }
}
