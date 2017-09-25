package com.feldman.blazej.presenter;


import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.dct.DCT;
import com.feldman.blazej.model.Logo;
import com.feldman.blazej.model.User;
import com.feldman.blazej.services.LogoService;
import com.feldman.blazej.util.JpegWriter;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@UIScope
@Component
public class LogoPresenter {

    @Autowired
    private LogoService logoService;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;
    @Autowired
    private DCT dct;

    public void addNewLogo(Logo logo){
        logoService.addLogo(logo);
    }

    public List<Logo> getAllLogoFromUser(User user){
        return logoService.findAllByUserId(user);
    }

    public Logo getLogoFromName(String name){
        return logoService.findLogoByName(name);
    }

    public void createLogoInTemporaryFile(String name){

        try {
            Logo logo = getLogoFromName(name);
            InputStream in = new ByteArrayInputStream(logo.getContent());
            BufferedImage bImageFromConvert = ImageIO.read(in);
            File outputfile = new File(applicationConfiguration.logoFormatter + "input.jpg");
            JpegWriter.createPicture(outputfile,"jpg",bImageFromConvert);
            in.close();
        } catch (IOException e) {
            Notification.show("Problem z logiem");
        }
    }
}
