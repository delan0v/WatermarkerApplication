package com.feldman.blazej.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Błażej on 27.10.2016.
 */
@Data
@Entity
@Table(name = "w_watermark")
public class Watermark {

    @Id
    @Column(name = "watermark_id")
    private String watermarkId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(name = "watermark_watermark")
    private byte[] watermarkBytes;

    @Column(name = "watermark_text")
    private String watermarkText;

}