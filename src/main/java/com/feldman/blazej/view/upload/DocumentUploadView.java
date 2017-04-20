package com.feldman.blazej.view.upload;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.presenter.DocumentPresenter;
import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.util.FileUtils;
import com.feldman.blazej.util.IncorrectFormatException;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.component.DocumentReceiver;
import com.feldman.blazej.view.component.MuiThemeView;
import com.feldman.blazej.view.component.WUploadPanel;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
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

@Component
@SpringView(name = ViewNames.DOCUMENT_UPLOAD_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentUploadView extends GridLayout implements View, Upload.StartedListener, Upload.SucceededListener, Upload.FailedListener {

    private static final Logger logger = LoggerFactory.getLogger(DocumentUploadView.class);

    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private DocumentPresenter documentPresenter;
    @Autowired
    private WatermarkPresenter watermarkPresenter;

    private Upload upload;
    private TextField textField;
    private Button leave;
    private Label infoLabel;
    private Panel mainPanel;
    private VerticalLayout mainVerticalLayout;
    private MuiThemeView muiThemeView;
    private HorizontalLayout middleHorizontalLayout;
    private HorizontalLayout footerLayout;
    private VerticalLayout rightPanel;

    @PostConstruct
    private void init() {

        setSizeFull();
        mainPanel = new Panel();
        mainPanel.setHeight("100%");
        mainPanel.setWidth("80%");

        mainVerticalLayout = new VerticalLayout();
        mainVerticalLayout.setSizeFull();

        muiThemeView = new MuiThemeView();
        muiThemeView.setSizeFull();

        middleHorizontalLayout = new HorizontalLayout();
        middleHorizontalLayout.setSizeFull();

        footerLayout = new HorizontalLayout();
        footerLayout.setSizeFull();

        rightPanel = new VerticalLayout();
        rightPanel.setSizeUndefined();

        WUploadPanel uploadPanel = new WUploadPanel("Dodaj dokument [Dozwolony format *.docx]", new DocumentReceiver(configuration.getFilepath()));
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        upload = uploadPanel.getUpload();
        upload.addFailedListener(this);
        upload.addSucceededListener(this);
        upload.addStartedListener(this);

        textField = new TextField();
        textField.setHeight("32");
        textField.setWidth("300");
        textField.setValue("");

        infoLabel = new Label("Podaj opis dokumentu: ");
        infoLabel.setHeight("32");
        infoLabel.setWidth("300");


        leave = new Button("Wyloguj");
        leave.setHeight("32");
        leave.setWidth("300");
        leave.addClickListener((Button.ClickListener)event-> {
            getUI().getNavigator().navigateTo(ViewNames.LOGIN_VIEW);
            AuthorizationUtils.saveUsernameInSession(null);
        });


        addComponent(mainPanel);
        mainPanel.setContent(mainVerticalLayout);

        rightPanel.addComponent(uploadPanel);
        rightPanel.addComponent(infoLabel);
        rightPanel.addComponent(textField);
        rightPanel.addComponent(leave);

        mainVerticalLayout.addComponent(muiThemeView);
        mainVerticalLayout.addComponent(middleHorizontalLayout);
        mainVerticalLayout.addComponent(footerLayout);

        middleHorizontalLayout.addComponent(rightPanel);

        setComponentAlignment(mainPanel,Alignment.MIDDLE_CENTER);
        mainVerticalLayout.setComponentAlignment(muiThemeView,Alignment.TOP_CENTER);
        mainVerticalLayout.setComponentAlignment(middleHorizontalLayout,Alignment.MIDDLE_CENTER);
        mainVerticalLayout.setComponentAlignment(footerLayout,Alignment.MIDDLE_CENTER);
        middleHorizontalLayout.setComponentAlignment(rightPanel,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(uploadPanel, Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(textField,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(infoLabel,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(leave,Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
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
        File file = new File("C:\\"+configuration.getFilepath()+"\\"+event.getFilename());
        logger.debug("<<< Zakończono procesowanie dokumentu.");

        try {
            watermarkPresenter.createWatermark(documentPresenter.saveNewDocument(event,file));
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
}
