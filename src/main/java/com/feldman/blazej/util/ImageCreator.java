package com.feldman.blazej.util;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.dct.DCT;
import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.qrCode.QRCode;
import com.feldman.blazej.qrCode.QRCodeNameGenerator;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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


    @Autowired
    private WatermarkPresenter watermarkPresenter;
    @Autowired
    ApplicationConfiguration applicationConfiguration;

    private InputStream inputStream;
    @Autowired
    private DCT dct;

    public String addImageToXwpf(XWPFDocument xwpfDocument, boolean watermark) throws IOException, NotFoundException, WriterException, InvalidFormatException {

        QRCodeNameGenerator qrCodeNameGenerator = new QRCodeNameGenerator();
        if(watermark==false) {
            qrCodeNameGenerator.setNameForPng();
            QRCode qrCode = new QRCode(watermarkPresenter.getQrCodeHash(), applicationConfiguration.qrcFilePath + qrCodeNameGenerator.getFileName());
            qrCode.create();

            inputStream = new FileInputStream(applicationConfiguration.qrcFilePath + qrCodeNameGenerator.getFileName());
            xwpfDocument.createParagraph().createRun().addPicture(inputStream, Document.PICTURE_TYPE_PNG, "qrcode", Units.toEMU(100), Units.toEMU(100));
            inputStream.close();

            OutputStream outputStream = new FileOutputStream(applicationConfiguration.docFilePath + "qrcode.docx");
            xwpfDocument.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }else{
            qrCodeNameGenerator.setNameForJpg();
            JpegWriter.createPicture(new File(applicationConfiguration.logoFormatter + "input2.jpg"),"jpg",dct.loadPicture(new File(applicationConfiguration.logoFormatter + "input.jpg")));
            dct.createWatermarkWithDct(new File(applicationConfiguration.logoFormatter + "input2.jpg"),new File(applicationConfiguration.qrcFilePath + qrCodeNameGenerator.getFileName()));

            inputStream = new FileInputStream(applicationConfiguration.qrcFilePath + qrCodeNameGenerator.getFileName());
            xwpfDocument.createParagraph().createRun().addPicture(inputStream, Document.PICTURE_TYPE_JPEG, "qrcode", Units.toEMU(100), Units.toEMU(100));
            inputStream.close();
        }

        OutputStream outputStream = new FileOutputStream(applicationConfiguration.docFilePath + "qrcode.docx");
        xwpfDocument.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return applicationConfiguration.docFilePath + "qrcode.docx";
    }
}
