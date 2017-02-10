package com.feldman.blazej.services;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.Watermark;

/**
 * Created by Błażej on 02.12.2016.
 */
public interface WatermarkService {

    void addWatermark(Watermark watermark);

    Watermark findByDocument(Document document);

    Watermark findById(String id);
}
