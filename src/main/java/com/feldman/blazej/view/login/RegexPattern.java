package com.feldman.blazej.view.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Błażej on 06.12.2016.
 */
public class RegexPattern {
    public static Pattern loginPattern = Pattern.compile("[a-zA-Z0-9]{5,15}");
    public static Pattern nameAndSurnamePattern = Pattern.compile("[A-Z][a-z]+");
    public static Pattern mailPattern = Pattern.compile("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
    public static Pattern passwordPattern = Pattern.compile("[a-zA-Z0-9]{6,15}");
    public static Matcher matcher;

    public static boolean checkName(String name){
        matcher = nameAndSurnamePattern.matcher(name);
        return matcher.matches();
    }
    public static boolean checkSurname(String surname){
        matcher = nameAndSurnamePattern.matcher(surname);
        return matcher.matches();
    }
    public static boolean checkLogin(String login) {
        matcher = loginPattern.matcher(login);
        return matcher.matches();
    }
        public static boolean checkMail(String mail){
            matcher = mailPattern.matcher(mail);
            return matcher.matches();
        }
    public static boolean checkPassword(String password){
        matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }
}