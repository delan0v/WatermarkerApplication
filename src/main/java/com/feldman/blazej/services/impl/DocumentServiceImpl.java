package com.feldman.blazej.services.impl;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import com.feldman.blazej.repository.DocumentRepository;
import com.feldman.blazej.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Błażej on 02.12.2016.
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    private List<Document> copylist;


    @Override
    public void addDocument(Document document) {
        documentRepository.save(document);
    }

    @Override
    public void updateDocument(Document document) {
        Document newDocument = documentRepository.getOne(document.getDocumentId());
        newDocument.setUserId(document.getUserId());
        newDocument.setName(document.getName());
        newDocument.setContent(document.getContent());
        newDocument.setDocumentId(document.getDocumentId());
        documentRepository.delete(document);
        documentRepository.save(newDocument);
    }

    @Override
    public Document findByName(String name) {
        return documentRepository.findByName(name);
    }

    @Override
    public List<Document> findAllByUserId(User userId) {

        return documentRepository.findByUserId(userId);
//        for (int i = 0; i < documentRepository.findAll().size(); i++) {
//            if (documentRepository.findAll().get(i).getUserId() == userId) {
//                copylist = documentRepository.findAll();
//            }
//        }
//        return copylist;
    }

    @Override
    public Document findByDocHashCode(String docHashCode) {
        return documentRepository.findByDocHashCode(docHashCode);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document findByNameAndUserId(String name, User user) {
        return documentRepository.findByNameAndUserId(name, user);
    }
}


