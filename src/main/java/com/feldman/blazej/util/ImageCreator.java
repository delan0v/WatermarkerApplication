package com.feldman.blazej.util;

import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.qrCode.QRCode;
import com.feldman.blazej.qrCode.QRCodeNameGenerator;
import com.feldman.blazej.qrCode.QRCodeReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdn;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Błażej on 23.12.2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImageCreator {

    public static String qrcFilePathShow =  "C:\\tmp\\qrcodesToRead\\";
    private static String qrcFilePath = "C:\\tmp\\qrcodes\\";
    private static String docFilePath = "C:\\tmp\\doc\\";

    @Autowired
    private WatermarkPresenter watermarkPresenter;


    public String addImageToXwpf(XWPFDocument xwpfDocument) throws IOException, NotFoundException, WriterException, InvalidFormatException {

        QRCodeNameGenerator qrCodeNameGenerator = new QRCodeNameGenerator();
        QRCode qrCode = new QRCode(watermarkPresenter.getQrCodeHash(), qrcFilePath + qrCodeNameGenerator.getFileName());
        qrCode.create();
        InputStream inputStream = new FileInputStream(qrcFilePath + qrCodeNameGenerator.getFileName());
        xwpfDocument.createParagraph().createRun().addPicture(inputStream, Document.PICTURE_TYPE_PNG, "qrcode", Units.toEMU(200), Units.toEMU(200));
        inputStream.close();

        OutputStream outputStream = new FileOutputStream(docFilePath + "qrcode.docx");
        xwpfDocument.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return docFilePath + "qrcode.docx";
    }
}
