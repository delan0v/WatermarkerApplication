package com.feldman.blazej.model;

import com.vaadin.ui.ComboBox;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Błażej on 27.10.2016.
 */
@Data
@Entity
@Table(name = "w_document")
public class Document implements Serializable {

    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "document_name")
    private String name;

    @Column(name = "document_document")
    private byte [] content;

    @Column(name = "document_hash_code")
    private String docHashCode;

    @Column(name = "document_protection")
    private String protection;

}