package com.feldman.blazej.view.upload;

import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.component.MuiThemeView;
import com.feldman.blazej.view.userInterface.DecoderView;
import com.feldman.blazej.view.userInterface.EncoderView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@SpringView(name = ViewNames.DOCUMENT_UPLOAD_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountView extends GridLayout implements View{

    private static final Logger logger = LoggerFactory.getLogger(AccountView.class);

    @Autowired
    private EncoderView encoderView;
    @Autowired
    private DecoderView decoderView;

    private Button uploadWatermarkButton;
    private Button readWatermarkFromDoc;
    private Button showUserDocuments;
    private Button help;
    private Button leave;
    private Panel mainPanel;
    private VerticalLayout mainVerticalLayout;
    private MuiThemeView muiThemeView;
    private HorizontalLayout middleHorizontalLayout;
    private HorizontalLayout footerLayout;
    private VerticalLayout rightLayout;

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

        encoderView.setSizeFull();

        middleHorizontalLayout = new HorizontalLayout();
        middleHorizontalLayout.setSizeFull();

        footerLayout = new HorizontalLayout();
        footerLayout.setSizeFull();

        rightLayout = new VerticalLayout();
        rightLayout.setSizeUndefined();
        rightLayout.setMargin(true);

        decoderView.setSizeFull();

        uploadWatermarkButton = new Button("Zabezpiecz dokument");
        uploadWatermarkButton.setHeight("32");
        uploadWatermarkButton.setWidth("250");
        uploadWatermarkButton.addClickListener((Button.ClickListener)event->{
            decoderView.setVisible(true);
            encoderView.setVisible(false);

        });

        readWatermarkFromDoc = new Button("Sprawdź dokument");
        readWatermarkFromDoc.setWidth("250");
        readWatermarkFromDoc.setHeight("32");
        readWatermarkFromDoc.addClickListener((Button.ClickListener)event->{
            decoderView.setVisible(false);
            encoderView.setVisible(true);
        });

        showUserDocuments = new Button("Moje dokumenty");
        showUserDocuments.setHeight("32");
        showUserDocuments.setWidth("250");
        showUserDocuments.addClickListener((Button.ClickListener)event->{

        });

        help = new Button("Pomoc");
        help .setHeight("32");
        help.setWidth("250");
        help.addClickListener((Button.ClickListener)event->{

        });

        leave = new Button("Wyloguj");
        leave.setHeight("32");
        leave.setWidth("250");
        leave.addClickListener((Button.ClickListener)event-> {
            getUI().getNavigator().navigateTo(ViewNames.LOGIN_VIEW);
            AuthorizationUtils.saveUsernameInSession(null);
        });


        addComponent(mainPanel);
        mainPanel.setContent(mainVerticalLayout);

        rightLayout.addComponent(uploadWatermarkButton);
        rightLayout.addComponent(readWatermarkFromDoc);
        rightLayout.addComponent(showUserDocuments);
        rightLayout.addComponent(help);
        rightLayout.addComponent(leave);

        mainVerticalLayout.addComponent(muiThemeView);
        mainVerticalLayout.addComponent(middleHorizontalLayout);
        mainVerticalLayout.addComponent(footerLayout);

        middleHorizontalLayout.addComponent(decoderView);
        middleHorizontalLayout.addComponent(encoderView);
        middleHorizontalLayout.addComponent(rightLayout);

        setComponentAlignment(mainPanel,Alignment.MIDDLE_CENTER);

        mainVerticalLayout.setComponentAlignment(muiThemeView,Alignment.TOP_CENTER);
        mainVerticalLayout.setComponentAlignment(middleHorizontalLayout,Alignment.MIDDLE_CENTER);
        mainVerticalLayout.setComponentAlignment(footerLayout,Alignment.MIDDLE_CENTER);

        middleHorizontalLayout.setComponentAlignment(decoderView,Alignment.MIDDLE_CENTER);
        middleHorizontalLayout.setComponentAlignment(rightLayout,Alignment.TOP_RIGHT);
        middleHorizontalLayout.setComponentAlignment(encoderView,Alignment.MIDDLE_CENTER);

        rightLayout.setComponentAlignment(uploadWatermarkButton,Alignment.MIDDLE_CENTER);
        rightLayout.setComponentAlignment(readWatermarkFromDoc,Alignment.MIDDLE_CENTER);
        rightLayout.setComponentAlignment(showUserDocuments,Alignment.MIDDLE_CENTER);
        rightLayout.setComponentAlignment(help,Alignment.MIDDLE_CENTER);
        rightLayout.setComponentAlignment(leave,Alignment.MIDDLE_CENTER);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        encoderView.setVisible(false);
        decoderView.setVisible(false);
    }
}
