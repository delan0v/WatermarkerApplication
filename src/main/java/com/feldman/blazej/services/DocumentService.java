package com.feldman.blazej.services;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;

import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
public interface DocumentService {

    void addDocument(Document document);

    void updateDocument(Document document);

    Document findByName(String name);

    List<Document> findAllByUserId(User userId);

    Document findByDocHashCode(String md5Code);
}
