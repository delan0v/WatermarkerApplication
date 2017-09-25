package com.feldman.blazej.services.impl;

import com.feldman.blazej.model.Logo;
import com.feldman.blazej.model.User;
import com.feldman.blazej.repository.LogoRepository;
import com.feldman.blazej.services.LogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogoServiceImpl implements LogoService {

    @Autowired
    private LogoRepository logoRepository;
    @Override
    public void addLogo(Logo logo) {
        logoRepository.save(logo);
    }

    @Override
    public List<Logo> findAllByUserId(User userId) {
        return logoRepository.findByUserId(userId);
    }

    @Override
    public Logo findLogoByName(String name) {
        return logoRepository.findByName(name);
    }
}
