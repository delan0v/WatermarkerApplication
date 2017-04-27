package com.feldman.blazej.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Błażej on 31.12.2016.
 */
public class HashCoder {

    public static String getMd5Hash(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(bytesOfMessage).toString();
    }
    public static String getMd5Hash(byte [] bytesOfMessage) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(bytesOfMessage).toString();
    }

}
