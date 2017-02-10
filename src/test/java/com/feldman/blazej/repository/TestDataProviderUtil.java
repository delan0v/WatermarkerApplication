package com.feldman.blazej.repository;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import com.feldman.blazej.model.Watermark;

/**
 * Created by Błażej on 30.11.2016.
 */
public class TestDataProviderUtil {

    public static User newTestUser() {
        User user = new User();

        user.setName("Name");
        user.setSurname("SureName");
        user.setEmail("test@test.pl");
        user.setUserLogin("login");
        user.setPassword("password");

        return user;
    }

    public static Document newTestDocument() {
        User user = newTestUser();
        Document document = new Document();

        document.setName("documentName");
        document.setContent("documentsContent");
        document.setUserId(user);

        return document;
    }

    public static Watermark newTestWatermark() {
        Watermark watermark = new Watermark();
        Document document = newTestDocument();

        watermark.setDocument(document);
        watermark.setWatermarkBytes(new byte[]{(byte) 2313});

        return watermark;
    }
}
