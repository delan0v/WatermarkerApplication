package com.feldman.blazej.services.impl;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.Watermark;
import com.feldman.blazej.repository.WatermarkRepository;
import com.feldman.blazej.services.WatermarkService;
import com.feldman.blazej.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
@Service
public class WatermarkServiceImpl implements WatermarkService {

    @Autowired
    private WatermarkRepository watermarkRepository;

    @Override
    public void addWatermark(Watermark watermark) {
        watermarkRepository.save(watermark);
    }

    @Override
    public Watermark findByDocument(Document document) {
        return watermarkRepository.findByDocument(document);
    }

    @Override
    public Watermark findById(String id) {
        return watermarkRepository.findByWatermarkId(id);
    }

    @Override
    public List<Watermark> findAll(){
        return watermarkRepository.findAll();
    }

    @Override
    public Watermark findByHash(String id) {
        return watermarkRepository.findByWatermarkHash(id);
    }

    @Override
    public Document searchLastId() {
        return watermarkRepository.findFirstByOrderByDocumentDesc();
    }

}
