package com.feldman.blazej.repository;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Document findByName(String name);

    Document findByNameAndUserId(String name, User user);

    Document findByDocHashCode(String docHashCode);

}
