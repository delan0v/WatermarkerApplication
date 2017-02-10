package com.feldman.blazej.qrCode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Błażej on 11.11.2016.
 */
public class QRCode {

    private String qrCode;
    private String filePath;

    public QRCode(String qrCode, String filePatch) throws NotFoundException, IOException, WriterException {
        this.qrCode = qrCode;
        this.filePath = filePatch;
    }

    public void create() throws IOException, WriterException, NotFoundException {
        QRCodeReader qrCodeReader = new QRCodeReader();

        String charset = "UTF-8";

        Map<EncodeHintType, ErrorCorrectionLevel> mapper = new HashMap<>();
        mapper.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        qrCodeReader.writeQRCode(qrCode, filePath, charset, mapper, 200, 200);
    }
}
