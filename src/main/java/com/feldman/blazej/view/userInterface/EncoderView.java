package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.presenter.DocumentPresenter;
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
import java.io.IOException;

/**
 * Created by Błażej on 05.02.2017.
 */
@Component
@SpringView(name = ViewNames.ENCODER_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EncoderView extends VerticalLayout implements View, Upload.StartedListener, Upload.SucceededListener, Upload.FailedListener {

    private static final Logger logger = LoggerFactory.getLogger(AccountView.class);

    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private DocumentPresenter documentPresenter;

    private Upload upload2;
    private TextField encodeText;

    @PostConstruct
    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        WUploadPanel uploadPanel2 = new WUploadPanel("Dodaj dokument do odkodowania [Dozwolony format *.docx]", new DocumentReceiver(configuration.getFilepath()));
        VerticalLayout verticalLayout = uploadPanel2.getLayoutContainer();
        upload2 = uploadPanel2.getUpload();
        upload2.addFailedListener(this);
        upload2.addSucceededListener(this);
        upload2.addStartedListener(this);

        encodeText = new TextField();
        encodeText.setWidth("400");
        encodeText.setHeight("32");
        encodeText.setValue("");

        addComponent(uploadPanel2);
        verticalLayout.addComponent(encodeText);

        setComponentAlignment(uploadPanel2,Alignment.MIDDLE_CENTER);
        verticalLayout.setComponentAlignment(encodeText,Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        logger.warn("Upload dokumentu zakończony niepowodzeniem");
    }

    @Override
    public void uploadStarted(Upload.StartedEvent startedEvent) {
        logger.debug("Rozpoczynam upload2 dokumentu");
        if (!FileUtils.isAllowedExtenstion(startedEvent.getFilename())) {
            logger.warn("Niepoprawne rozszerzenie pliku {}, przerywam pobieranie", startedEvent.getFilename());
            Notification.show("Niepoprawne rozszerzenie pliku");
            upload2.interruptUpload();
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
        logger.debug("Upload dokumentu zakończony");
        logger.debug("Procesowanie dokumentu... >>>");
        logger.debug("<<< Zakończono procesowanie dokumentu.");
        try {
            encodeText.setValue(documentPresenter.showDocument(succeededEvent));
        } catch (IncorrectFormatException e) {
            Notification.show("Problem z formatem");
            e.printStackTrace();
        } catch (WriterException e) {
            Notification.show("Problem z odczytem");
            e.printStackTrace();
        } catch (NotFoundException e) {
            Notification.show("Nie znaleziono dokumentu");
            e.printStackTrace();
        } catch (IOException e) {
            Notification.show("Problem wejścia/wyjścia");
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            Notification.show("Niezgodny format");
            e.printStackTrace();
        }
    }
}