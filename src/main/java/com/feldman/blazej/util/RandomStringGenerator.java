package com.feldman.blazej.util;

import java.util.Random;

public class RandomStringGenerator {
    public static String randVal;
public static String generateRandomValue(){
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 20; i++) {
        char c = chars[random.nextInt(chars.length)];
        sb.append(c);
    }
        randVal = sb.toString();
        return randVal;
    }
}
