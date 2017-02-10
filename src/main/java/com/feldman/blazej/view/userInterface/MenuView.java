package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Błażej on 28.01.2017.
 */
@SpringView(name = ViewNames.MENU_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MenuView extends VerticalLayout implements View {

    private Button leave;
    private Button wUploadPanelButton;
    private Button decoderViewButton;

    public MenuView(){
        setMargin(true);
        setSpacing(true);

    }

    @PostConstruct
    private void init(){

        leave = new Button("Wyloguj się");
        leave.setHeight("30");
        leave.setWidth("250");
        leave.addClickListener((Button.ClickListener)event-> {
            AuthorizationUtils.saveUsernameInSession(null);
            getUI().getNavigator().navigateTo(ViewNames.LOGIN_VIEW);
        });

        wUploadPanelButton = new Button("Watermarking dokumentu");
        wUploadPanelButton.setHeight("30");
        wUploadPanelButton.setWidth("250");
        wUploadPanelButton.addClickListener((Button.ClickListener)event->{
            getUI().getNavigator().navigateTo(ViewNames.DOCUMENT_UPLOAD_VIEW);
        });

        decoderViewButton = new Button("Dekoduj wiadomość");
        decoderViewButton.setHeight("30");
        decoderViewButton.setWidth("250");
        decoderViewButton.addClickListener((Button.ClickListener)event->{
            getUI().getNavigator().navigateTo(ViewNames.ENCODER_VIEW);
        });

        addComponent(wUploadPanelButton);
        addComponent(decoderViewButton);
        addComponent(leave);

        setComponentAlignment(wUploadPanelButton, Alignment.MIDDLE_CENTER);
        setComponentAlignment(decoderViewButton,Alignment.MIDDLE_CENTER);
        setComponentAlignment(leave,Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
