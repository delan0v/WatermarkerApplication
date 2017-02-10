package com.feldman.blazej.repository;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.Watermark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatermarkRepository extends JpaRepository<Watermark,String> {
    Watermark findByDocument(Document document);
    Watermark findByWatermarkId(String watermarkId);
}