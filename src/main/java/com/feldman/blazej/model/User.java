package com.feldman.blazej.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by Błażej on 27.10.2016.
 */
@Data
@Entity
@Table(name = "w_user")
public class User{

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_surname")
    private String surname;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_password")
    private String password;
}