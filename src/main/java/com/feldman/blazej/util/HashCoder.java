package com.feldman.blazej.util;

import org.apache.xmlbeans.impl.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Błażej on 31.12.2016.
 */
public class HashCoder {
    public static byte[] getSha256Digest(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(s.getBytes("UTF-8"));
        return Base64.encode(messageDigest.digest());
    }
    public static String getSha256DigestAsString(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = getSha256Digest(s);
        String base64 = new String(bytes, "UTF-8");
        return base64;
    }
}
