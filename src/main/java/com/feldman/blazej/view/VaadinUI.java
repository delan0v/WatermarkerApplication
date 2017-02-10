package com.feldman.blazej.view;

import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.login.LoginView;
import com.feldman.blazej.view.newUser.NewUserView;
import com.feldman.blazej.view.upload.DocumentUploadView;
import com.feldman.blazej.view.userInterface.EncoderView;
import com.feldman.blazej.view.userInterface.MenuView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Błażej on 27.10.2016.
 */
@Theme("valo")
@SpringUI
public class VaadinUI extends UI {

    private Navigator navigator;
    @Autowired
    private LoginView loginView;
    @Autowired
    private DocumentUploadView documentView;
    @Autowired
    private NewUserView newUserView;
    @Autowired
    private MenuView menuView;
    @Autowired
    private EncoderView encoderView;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        navigator = new Navigator(this, this);
        navigator.addView(ViewNames.LOGIN_VIEW, loginView);
        navigator.addView(ViewNames.DOCUMENT_UPLOAD_VIEW, documentView);
        navigator.addView(ViewNames.NEW_USER_VIEW, newUserView);
        navigator.addView(ViewNames.MENU_VIEW,menuView);
        navigator.addView(ViewNames.ENCODER_VIEW,encoderView);
    }
}
