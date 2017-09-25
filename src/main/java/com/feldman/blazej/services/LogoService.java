package com.feldman.blazej.services;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.Logo;
import com.feldman.blazej.model.User;

import java.util.List;

public interface LogoService {

    void addLogo(Logo logo);

    List<Logo> findAllByUserId(User userId);

    Logo findLogoByName(String name);
}
