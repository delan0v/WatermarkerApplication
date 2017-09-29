package com.feldman.blazej.qrCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Błażej on 11.11.2016.
 */
public class QRCoder {

    public void writeQRCode
            (String qrCodeData, String filePath, String charset,
                            Map hintMap, int qrCodeheight, int qrCodewidth)
                                throws WriterException, IOException {
                        BitMatrix matrix = new MultiFormatWriter().encode
                         (new String(qrCodeData.getBytes
                (charset), charset), BarcodeFormat.QR_CODE, qrCodewidth
                        , qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring
                (filePath.lastIndexOf('.') + 1), new File(filePath));
    }

    public String readQRCode(String filePath, Map hintMap)
            throws IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer
                (new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
        return qrCodeResult.getText();
    }
}
