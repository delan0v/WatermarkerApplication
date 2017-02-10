package com.feldman.blazej.view.newUser;

import com.feldman.blazej.presenter.UserPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Błażej on 01.12.2016.
 */
@SpringView(name = ViewNames.NEW_USER_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewUserView extends VerticalLayout implements View {

    @Autowired
    UserPresenter userPresenter;

    private Button back;
    private TextField name;
    private TextField surname;
    private TextField email;
    private TextField userLogin;
    private TextField password;
    private Button createUser;
    private String flag;

    public NewUserView() {
        setMargin(true);
        setSpacing(true);
    }

    @PostConstruct
    public void initView() {
        back = new Button("Cofnij");
        back.setWidth("250");
        back.setHeight("30");
        back.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                getUI().getNavigator().navigateTo(ViewNames.LOGIN_VIEW);
            }
        });
        name = new TextField("Imię");
        name.setWidth("250");
        name.setHeight("30");

        surname = new TextField("Nazwisko");
        surname.setHeight("30");
        surname.setWidth("250");

        email = new TextField("E-mail");
        email.setHeight("30");
        email.setWidth("250");

        userLogin = new TextField("Login");
        userLogin.setHeight("30");
        userLogin.setWidth("250");

        password = new TextField("Hasło");
        password.setHeight("30");
        password.setWidth("250");

        createUser = new Button("Utwórz konto");
        createUser.setHeight("30");
        createUser.setWidth("250");
        createUser.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                flag = userPresenter.checkUser(userLogin.getValue(),email.getValue(),name.getValue(),surname.getValue(),password.getValue());
                if(flag.equals("")){
                    getUI().getNavigator().navigateTo(ViewNames.DOCUMENT_UPLOAD_VIEW);
                    AuthorizationUtils.saveUsernameInSession(userLogin.getValue());
                }
                Notification.show(flag);
            }
        });

        addComponent(back);
        addComponent(name);
        addComponent(surname);
        addComponent(email);
        addComponent(userLogin);
        addComponent(password);
        addComponent(createUser);

        setComponentAlignment(back, Alignment.TOP_LEFT);
        setComponentAlignment(name, Alignment.MIDDLE_CENTER);
        setComponentAlignment(surname, Alignment.MIDDLE_CENTER);
        setComponentAlignment(email, Alignment.MIDDLE_CENTER);
        setComponentAlignment(userLogin, Alignment.MIDDLE_CENTER);
        setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        setComponentAlignment(createUser, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
