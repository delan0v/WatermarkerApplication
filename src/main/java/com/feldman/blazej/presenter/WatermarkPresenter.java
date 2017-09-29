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
    public static Double WatermarkCode;

    public static int indexNumber;


    public void addNewWatermark(Watermark watermark) {
        watermarkService.addWatermark(watermark);
    }

    public Watermark searchWatermarkByDocument(Document document) {
        return watermarkService.findByDocument(document);
    }

    public Watermark searchWatermarkByHash(String id){ return watermarkService.findByHash(id);}

    public Watermark searchWatermarkById(String id){ return watermarkService.findByHash(id);}

    public Watermark searchWatermarkWith(double dct){
        List<Watermark> list = watermarkService.findAll();
        for(Watermark a:list){
            Double z = a.getWatermarkDct();
            if(z==null){

            }
            else if(z==dct){
                return a;
            }
//                if ((z*0.9 < dct)&&(z*1.1>dct)) {
//
//                    return a;
//                }
        }
        return null;
    }

    public void createWatermark(Document document, boolean isWatermark) throws WriterException, InvalidFormatException, NotFoundException, IOException {

        Watermark watermark = new Watermark();
        watermark.setDocument(document);
        watermark.setWatermarkText(generateWatermarkText(document));
        if (isWatermark==true) {
            watermark.setWatermarkDct(WatermarkCode);
        }else{
            watermark.setWatermarkDct(0.0);
        }
        watermark.setWatermarkHash(getQrCodeHash());
        watermark.setWatermarkBytes(new byte[0]);
        watermark.setWatermarkId(document.getDocHashCode());
        addNewWatermark(watermark);
    }

    public Long searchLastId(){
        List <Watermark> list = watermarkService.findAll();
        if(list.size()==0){
            return Long.valueOf(0);
        }
        Watermark b = null;
        for(Watermark a:list){
            if(b==null){
                b=a;
            }
            else{
                if(a.getDocument().getDocumentId()>b.getDocument().getDocumentId()){
                    b=a;
                }
            }
        }
        return b.getDocument().getDocumentId();
    }

    public String getDecodeText(String id){
        return searchWatermarkByHash(id).getWatermarkText();
    }
    public String generateWatermarkText(Document document){
        User user = userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession());
        return "Imię: "+user.getName()+"%$Nazwisko: "+user.getSurname()+"%$Data utworzenia: "+ LocalDate.now().format(dateFormat)+" "
                + LocalTime.now().format(timeFormat)+"%$Opis: "+ getQrCodeContentText()+"%$Hash: "+document.getDocHashCode();
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
    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        WatermarkPresenter.indexNumber = indexNumber;
    }
}

