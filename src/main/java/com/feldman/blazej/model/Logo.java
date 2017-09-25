package com.feldman.blazej.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "w_logo")
public class Logo {

    @Id
    @Column(name = "logo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logoId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "logo_content")
    private byte [] content;

    @Column(name = "logo_name")
    private String name;
}
