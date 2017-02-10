package com.feldman.blazej.qrCode;

import com.feldman.blazej.util.StringGenerator;
public class QRCodeNameGenerator {

    private static int id = 1;
    private String localization;
    private String fileName;

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public QRCodeNameGenerator(){

        localization = StringGenerator.LOCALIZATION;
        fileName = "qrcode" + id + ".png";
        id++;
    }
}
