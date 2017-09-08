package com.feldman.blazej.presenter;

import com.feldman.blazej.dct.DCT;
import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import com.feldman.blazej.model.Watermark;
import com.feldman.blazej.services.WatermarkService;
import com.feldman.blazej.util.AuthorizationUtils;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.vaadin.spring.annotation.UIScope;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
@UIScope
@Component
public class WatermarkPresenter {

    @Autowired
    private WatermarkService watermarkService;
    @Autowired
    private UserPresenter userPresenter;
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("kk:mm:ss");

    public static String QrCodeContentText;
    public static String QrCodeHash;


    public void addNewWatermark(Watermark watermark) {
        watermarkService.addWatermark(watermark);
    }

    public Watermark searchWatermarkByDocument(Document document) {
        return watermarkService.findByDocument(document);
    }

    public Watermark searchWatermarkById(String id){ return watermarkService.findById(id);}

    public Watermark searchWatermarkWith(double dct){
        List<Watermark> list = watermarkService.findAll();
        for(Watermark a:list){
            Double z = a.getWatermarkDct();
                if ((z*0.9 < dct)&&(z*1.1>dct)) {

                    return a;
                }
        }
        return null;
    }

    public void createWatermark(Document document, boolean isWatermark) throws WriterException, InvalidFormatException, NotFoundException, IOException {

        Watermark watermark = new Watermark();
        watermark.setDocument(document);
        watermark.setWatermarkText(generateWatermarkText());
        if (isWatermark==true) {
            watermark.setWatermarkDct(DCT.watermarkParam);
        }else{
            watermark.setWatermarkDct(0.0);
        }
        watermark.setWatermarkBytes(new byte[0]);
        watermark.setWatermarkId(document.getDocHashCode());
        addNewWatermark(watermark);
    }
    public String getDecodeText(String id){
        return searchWatermarkById(id).getWatermarkText();
    }
    public String generateWatermarkText(){
        User user = userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession());
        return "Imię: "+user.getName()+"\n Nazwisko: "+user.getSurname()+"\n Data utworzenia: "+ LocalDate.now().format(dateFormat)+" "+ LocalTime.now().format(timeFormat)+"\n Opis: "+ getQrCodeContentText();
    }
    public String getQrCodeContentText() {
        return QrCodeContentText;
    }

    public void setQrCodeContentText(String qrCodeContentText) {
        QrCodeContentText = qrCodeContentText;
    }
    public static String getQrCodeHash() {
        return QrCodeHash;
    }

    public static void setQrCodeHash(String qrCodeHash) {
        QrCodeHash = qrCodeHash;
    }
}

