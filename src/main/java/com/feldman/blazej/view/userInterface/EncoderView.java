package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.presenter.DocumentPresenter;
import com.feldman.blazej.util.FileUtils;
import com.feldman.blazej.util.IncorrectFormatException;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.component.DocumentReceiver;
import com.feldman.blazej.view.component.WUploadPanel;
import com.feldman.blazej.view.upload.DocumentUploadView;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
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
import java.io.IOException;

/**
 * Created by Błażej on 05.02.2017.
 */
@Component
@SpringView(name = ViewNames.ENCODER_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EncoderView extends VerticalLayout implements View, Upload.StartedListener, Upload.SucceededListener, Upload.FailedListener {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadView.class);

    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private DocumentPresenter documentPresenter;

    private Upload upload2;
    private TextField encodeText;
    private Button leave;

    @PostConstruct
    private void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);
        setCaption("Dodawanie dokumentu...");
        WUploadPanel uploadPanel2 = new WUploadPanel("Dodaj dokument [Dozwolony format *.doc lub *.docx]", new DocumentReceiver(configuration.getFilepath()));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMargin(true);
        horizontalLayout.setSpacing(true);

        upload2 = uploadPanel2.getUpload();
        upload2.addFailedListener(this);
        upload2.addSucceededListener(this);
        upload2.addStartedListener(this);

        leave = new Button("Cofnij");
        leave.setHeight("30");
        leave.setWidth("250");
        leave.addClickListener((Button.ClickListener)event-> {
            getUI().getNavigator().navigateTo(ViewNames.MENU_VIEW);
        });

        encodeText = new TextField();
        encodeText.setWidth("700");
        encodeText.setHeight("30");
        encodeText.setValue("");

        Label content = new Label("Dane dokumentu: ");
        content.setHeight("30");
        content.setWidth("150");

        addComponent(uploadPanel2);
        addComponent(horizontalLayout);
        addComponent(leave);

        horizontalLayout.addComponent(content);
        horizontalLayout.addComponent(encodeText);

        horizontalLayout.setComponentAlignment(encodeText,Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(content,Alignment.MIDDLE_CENTER);

        setComponentAlignment(uploadPanel2, Alignment.MIDDLE_CENTER);
        setComponentAlignment(leave,Alignment.MIDDLE_RIGHT);
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