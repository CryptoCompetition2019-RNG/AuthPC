package org.gmssl.java;

import java.io.InputStream;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileInputStream;

/*
 * byte[] msg;
 * String filePath = new String("E:/Poem.txt");
 * byte[] hash1 = (new SM3Hash()).fileSM3(msg);
 * byte[] hash2 = (new SM3Hash()).fileSM3(filePath);
 *
 */
public class SM3Hash {
    public SM3Hash() {

    }

    public byte[] bytesSM3(byte[] msg) {
        byte[] md = new byte[32];
        SM3Digest sm3 = new SM3Digest();
        sm3.update(msg, 0, msg.length);
        sm3.doFinal(md, 0);
        return md;
    }

    /**
     * byte[] hash1 = (new SM3Hash()).fileSM3(filePath);
     */
    public String fileSM3(String filePath) {
        byte[] bs = null;
        try {
            //�����ļ�
            File infile = new File(filePath);
            InputStream inputStream = new FileInputStream(infile);
            bs = new byte[(int) infile.length()];
            inputStream.read(bs);
            inputStream.close();
            //ȡHash
            byte[] Hash = bytesSM3(bs);
            return ConvertUtil.getHexString(Hash);
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return null;
        }
    }

    /**
     * byte[] hash1 = (new SM3Hash()).stringSM3(msg);
     */
    public String stringSM3(String input) {
        byte[] bs = new byte[0];
        bs = bytesSM3(input.getBytes());
        String cipherText = ConvertUtil.getHexString(bs);
        if (cipherText != null && cipherText.trim().length() > 0) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(cipherText);
            cipherText = m.replaceAll("");
        }
        return cipherText;
    }

}

