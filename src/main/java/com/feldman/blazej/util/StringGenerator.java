package com.feldman.blazej.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Błażej on 28.01.2017.
 */
public class StringGenerator {

    public static String QR_TEXT ="";
    public static String LOCALIZATION ="";

    public StringGenerator(int length){
        SecureRandom random = new SecureRandom();
        LOCALIZATION = new BigInteger(130, random).toString(length);
    }
}
