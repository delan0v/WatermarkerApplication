package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.presenter.DocumentPresenter;
import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.util.FileUtils;
import com.feldman.blazej.util.IncorrectFormatException;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.component.DocumentReceiver;
import com.feldman.blazej.view.component.WUploadPanel;
import com.feldman.blazej.view.upload.AccountView;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Błażej on 27.04.2017.
 */
@Component
@SpringView(name = ViewNames.DECODER_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DecoderView extends VerticalLayout implements View, Upload.StartedListener, Upload.SucceededListener, Upload.FailedListener {

    private static final Logger logger = LoggerFactory.getLogger(AccountView.class);

    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private DocumentPresenter documentPresenter;
    @Autowired
    private WatermarkPresenter watermarkPresenter;

    private Upload upload;
    private TextField textField;
    private Label infoLabel;


    @PostConstruct
    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        WUploadPanel uploadPanel2 = new WUploadPanel("Dodaj dokument do zakodowania [Dozwolony format *.docx]", new DocumentReceiver(configuration.getFilepath()));
        VerticalLayout verticalLayout = uploadPanel2.getLayoutContainer();
        upload = uploadPanel2.getUpload();
        upload.addFailedListener(this);
        upload.addSucceededListener(this);
        upload.addStartedListener(this);

        textField = new TextField();
        textField.setHeight("32");
        textField.setWidth("400");
        textField.setValue("");

        infoLabel = new Label("Podaj opis dokumentu: ");
        infoLabel.setHeight("32");
        infoLabel.setWidth("400");

        addComponent(uploadPanel2);
        verticalLayout.addComponent(infoLabel);
        verticalLayout.addComponent(textField);
        setComponentAlignment(uploadPanel2, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        logger.warn("Upload dokumentu zakończony niepowodzeniem");
    }

    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        logger.debug("Rozpoczynam upload dokumentu");
        if (!FileUtils.isAllowedExtenstion(event.getFilename())) {
            logger.warn("Niepoprawne rozszerzenie pliku {}, przerywam pobieranie", event.getFilename());
            Notification.show("Niepoprawne rozszerzenie pliku");
            upload.interruptUpload();
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        watermarkPresenter.setQrCodeContentText(textField.getValue());
        logger.debug("Upload dokumentu zakończony");
        logger.debug("Procesowanie dokumentu... >>>");
        File file = new File("C:\\" + configuration.getFilepath() + "\\" + event.getFilename());
        logger.debug("<<< Zakończono procesowanie dokumentu.");

        try {
            watermarkPresenter.createWatermark(documentPresenter.saveNewDocument(event, file));
            Notification.show("Dokument został zapisany w bazie danych");
            logger.debug("Dokument został zapisany w bazie danych");
        } catch (UnsupportedEncodingException e) {
            Notification.show("Upload do bazy danych nie powiódł się (UnsupportedEncodingException)");
            logger.error("Zapisanie dokumentu nie powieodło się: UnsupportedEncodingException");
        } catch (NoSuchAlgorithmException e) {
            Notification.show("Upload do bazy danych nie powiódł się (NoSuchAlgorithmException)");
            logger.error("Zapisanie dokumentu nie powieodło się: NoSuchAlgorithmException");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncorrectFormatException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}