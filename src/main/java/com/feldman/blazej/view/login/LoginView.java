package com.feldman.blazej.view.login;

import com.feldman.blazej.model.User;
import com.feldman.blazej.presenter.UserPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.component.MuiThemeView;
import com.feldman.blazej.view.userInterface.EncoderView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Błażej on 27.10.2016.
 */

@SpringView(name = ViewNames.LOGIN_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginView extends GridLayout implements View {

    @Autowired
    public UserPresenter userPresenter;
    @Autowired
    public EncoderView encoderView;
    @Autowired
    public NewUserView newUserView;

    private TextField login;
    private PasswordField passwordField;
    private Button logIn;
    private Button newUser;
    private Button link;
    private Button getWatermarkerReader;
    private Panel mainPanel;
    private VerticalLayout mainVerticalLayout;
    private MuiThemeView muiThemeView;
    private HorizontalLayout middleHorizontalLayout;
    private HorizontalLayout footerLayout;
    private VerticalLayout rightPanel;
    private Button helper;

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
        newUserView.setSizeUndefined();

        middleHorizontalLayout = new HorizontalLayout();
        middleHorizontalLayout.setSizeFull();

        footerLayout = new HorizontalLayout();
        footerLayout.setSizeFull();

        rightPanel = new VerticalLayout();
        rightPanel.setSizeUndefined();
        rightPanel.setMargin(true);

        login = new TextField("Login:");
        login.setHeight("32");
        login.setWidth("250");

        passwordField = new PasswordField("Hasło:");
        passwordField.setHeight("32");
        passwordField.setWidth("250");

        logIn = new Button("Zaloguj");
        logIn.setHeight("32");
        logIn.setWidth("250");
        logIn.addClickListener((Button.ClickListener) event -> {
            User user = userPresenter.searchUserByLoginAndPassword(login.getValue(), passwordField.getValue());
            if (user != null) {
                AuthorizationUtils.saveUsernameInSession(user.getUserLogin());
                getUI().getNavigator().navigateTo(ViewNames.DOCUMENT_UPLOAD_VIEW);
            }
            else{
                Notification.show("Podałeś błędny login bądź hasło");
            }
        });

        newUser = new Button("Nowy użytkownik");
        newUser.setHeight("32");
        newUser.setWidth("250");
        newUser.addClickListener((Button.ClickListener)event->{
            encoderView.setVisible(false);
            newUserView.setVisible(true);
        });

        helper = new Button("Pomoc");
        helper.setHeight("32");
        helper.setWidth("250");
        helper.addClickListener((Button.ClickListener)event->{
            encoderView.setVisible(false);
            newUserView.setVisible(false);
        });

        getWatermarkerReader = new Button("Odczytaj watermark");
        getWatermarkerReader.setHeight("32");
        getWatermarkerReader.setWidth("250");
        getWatermarkerReader.addClickListener((Button.ClickListener)event->{
            encoderView.setVisible(true);
            newUserView.setVisible(false);
        });

        link = new Button(("Strona główna"));
        link.setHeight("32");
        link.setWidth("250");
        link.addClickListener((Button.ClickListener)event->{
            getUI().getPage().open("http:////www.wat.edu.pl/","wat");
        });

        addComponent(mainPanel);
        mainPanel.setContent(mainVerticalLayout);

        rightPanel.addComponent(login);
        rightPanel.addComponent(passwordField);
        rightPanel.addComponent(logIn);
        rightPanel.addComponent(newUser);
        rightPanel.addComponent(helper);
        rightPanel.addComponent(getWatermarkerReader);
        rightPanel.addComponent(link);

        mainVerticalLayout.addComponent(muiThemeView);
        mainVerticalLayout.addComponent(middleHorizontalLayout);
        mainVerticalLayout.addComponent(footerLayout);
        middleHorizontalLayout.addComponent(encoderView);
        middleHorizontalLayout.addComponent(newUserView);
        middleHorizontalLayout.addComponent(rightPanel);

        setComponentAlignment(mainPanel,Alignment.MIDDLE_CENTER);
        mainVerticalLayout.setComponentAlignment(muiThemeView,Alignment.TOP_CENTER);
        mainVerticalLayout.setComponentAlignment(middleHorizontalLayout,Alignment.MIDDLE_CENTER);
        mainVerticalLayout.setComponentAlignment(footerLayout,Alignment.MIDDLE_CENTER);
        middleHorizontalLayout.setComponentAlignment(rightPanel,Alignment.TOP_RIGHT);
        middleHorizontalLayout.setComponentAlignment(encoderView,Alignment.MIDDLE_CENTER);
        middleHorizontalLayout.setComponentAlignment(newUserView,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(login, Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(passwordField,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(logIn,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(newUser,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(helper,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(getWatermarkerReader,Alignment.MIDDLE_CENTER);
        rightPanel.setComponentAlignment(link,Alignment.MIDDLE_CENTER);

    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        login.setValue("");
        passwordField.setValue("");
        encoderView.setVisible(false);
        newUserView.setVisible(false);
    }
}