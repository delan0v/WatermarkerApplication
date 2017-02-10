package com.feldman.blazej.util;

import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final String DOT = "\\.";

    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList("doc", "docx");

    /**
     * Sprawdza czy rozszerzenie pliku jest dopuszczalne z określoną listą
     *
     * @param filename nazwa pliku
     * @return true jeśli jest rozszerzenie jest dopuszczalne, flase w przeciwnym wypadku
     */
    public static boolean isAllowedExtenstion(String filename) {

        String fileExtension = getFileExtension(filename);
        for (String extenstion : ALLOWED_FILE_EXTENSIONS) {
            if (extenstion.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metoda zwraca rozszerzenie pliku, które jest ostatnim elementem nazwy pliku po kropce
     *
     * @param filename nazwa pliku z rozszerzeniem
     * @return rozszerzenie pliku
     */
    public static String getFileExtension(String filename) {
        String[] splitedByDot = splitByDot(filename);
        if (splitedByDot.length > 1) {
            return splitedByDot[splitedByDot.length - 1];
        }
        return "";
    }

    /**
     * Metoda splituje tekst po kropce
     *
     * @param s String
     * @return tablica splitniętych wartości
     */
    public static String[] splitByDot(String s) {
        return s.split(DOT);
    }
}
