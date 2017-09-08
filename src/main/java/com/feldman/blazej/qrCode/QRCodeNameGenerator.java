package com.feldman.blazej.qrCode;

public class QRCodeNameGenerator {

    private static int id = 1;
    private static String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public  QRCodeNameGenerator() {
    }
    public void setNameForPng(){
            fileName = "qrcode" + id + ".png";
            id++;
    }
    public void setNameForJpg(){
        fileName = "qrcode" + id + ".jpg";
        id++;
    }
}
