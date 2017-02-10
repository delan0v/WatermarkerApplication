package com.feldman.blazej.presenter;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import com.feldman.blazej.model.Watermark;
import com.feldman.blazej.services.WatermarkService;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.util.StringGenerator;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.vaadin.spring.annotation.UIScope;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

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

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("kk:mm:ss");

    public void addNewWatermark(Watermark watermark) {
        watermarkService.addWatermark(watermark);
    }

    public Watermark searchWatermarkByDocument(Document document) {
        return watermarkService.findByDocument(document);
    }

    public Watermark searchWatermarkById(String id){ return watermarkService.findById(id);}

    public void createWatermark(Document document) throws WriterException, InvalidFormatException, NotFoundException, IOException {

        Watermark watermark = new Watermark();
        watermark.setWatermarkId(StringGenerator.LOCALIZATION);
        watermark.setDocument(document);
        watermark.setWatermarkText(generateWatermarkText());
        watermark.setWatermarkBytes(new byte[0]);
        addNewWatermark(watermark);
    }
    public String getDecodeText(String id){
        return searchWatermarkById(id).getWatermarkText();
    }
    public String generateWatermarkText(){
        User user = userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession());
        return "Imię: "+user.getName()+"\n Nazwisko: "+user.getSurname()+"\n Data utworzenia: "+ LocalDate.now().format(dateFormat)+" "+ LocalTime.now().format(timeFormat)+"\n Opis: "+StringGenerator.QR_TEXT;
    }
}

