package com.feldman.blazej.view.component;

import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.qrCode.QRCode;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.util.StringGenerator;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.upload.DocumentUploadView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.Getter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Klasa komponentu do ładowania dokumentów
 */
@SpringView(name = ViewNames.DOCUMENT_UPLOAD_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WUploadPanel extends Panel implements View {

    @Getter
    private final Upload upload;

    public WUploadPanel(String caption, Upload.Receiver receiver) {
        setSizeUndefined();
        setCaption(caption);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        verticalLayout.setSizeUndefined();

        upload = new Upload(null, receiver);
        upload.setWidth("800px");
        upload.setHeightUndefined();
        upload.setButtonCaption(null);
        verticalLayout.addComponent(upload);

        Button startUploadButton = new Button("Wybierz");
        startUploadButton.setHeight("30");
        startUploadButton.setWidth("250");
        startUploadButton.addClickListener((Button.ClickListener) event -> {
            upload.submitUpload();
        });

        verticalLayout.addComponent(startUploadButton);
        verticalLayout.setComponentAlignment(startUploadButton, Alignment.BOTTOM_RIGHT);

        setContent(verticalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
