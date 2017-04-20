package com.feldman.blazej.qrCode;

public class QRCodeNameGenerator {

    private static int id = 1;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public QRCodeNameGenerator(){

        fileName = "qrcode" + id + ".png";
        id++;
    }
}
