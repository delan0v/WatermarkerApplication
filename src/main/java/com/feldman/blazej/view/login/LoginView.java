package com.feldman.blazej.view.login;

import com.feldman.blazej.model.User;
import com.feldman.blazej.presenter.UserPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
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
 * Created by Błażej on 27.10.2016.
 */

@SpringView(name = ViewNames.LOGIN_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginView extends VerticalLayout implements View {

    @Autowired
    UserPresenter userPresenter;

    private TextField login;
    private PasswordField password;
    private Button executeLog;

    public LoginView() {
        setMargin(true);
        setSpacing(true);
        setStyleName("C\\:Progrejming\\Application\\src\\main\\webapp\\VAADIN\\themes\\mythme\\styles.css");
    }

    @PostConstruct
    private void init() {

        login = new TextField("Login:");
        login.setHeight("30");
        login.setWidth("250");
        login.setVisible(false);

        executeLog = new Button("Zaloguj");
        executeLog.setHeight("30");
        executeLog.setWidth("250");
        executeLog.setVisible(false);
        executeLog.addClickListener((Button.ClickListener) event -> {
            User user = userPresenter.searchUserByLoginAndPassword(login.getValue(), password.getValue());
            if (user != null) {
                AuthorizationUtils.saveUsernameInSession(user.getUserLogin());
                getUI().getNavigator().navigateTo(ViewNames.MENU_VIEW);
            }
            else{
                Notification.show("Podałeś błędny login bądź hasło");
            }
        });

        password = new PasswordField("Hasło:");
        password.setHeight("30");
        password.setWidth("250");
        password.setVisible(false);

        Button logIn = new Button("Logowanie");
        logIn.setWidth("250");
        logIn.setHeight("30");
        logIn.addClickListener((Button.ClickListener) event -> {
            login.setVisible(true);
            password.setVisible(true);
            executeLog.setVisible(true);
        });

        Button newAccount = new Button("Nowy użytkownik");
        newAccount.setWidth("250");
        newAccount.setHeight("30");
        newAccount.addClickListener((Button.ClickListener) event -> getUI().getNavigator().navigateTo(ViewNames.NEW_USER_VIEW));

        addComponent(logIn);
        addComponent(newAccount);
        addComponent(login);
        addComponent(password);
        addComponent(executeLog);

        setComponentAlignment(logIn, Alignment.TOP_LEFT);
        setComponentAlignment(newAccount, Alignment.TOP_LEFT);
        setComponentAlignment(login, Alignment.BOTTOM_CENTER);
        setComponentAlignment(password, Alignment.BOTTOM_CENTER);
        setComponentAlignment(executeLog, Alignment.BOTTOM_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        login.setValue("");
        password.setValue("");
        Notification.show("Witaj na naszej stronie!");
    }
}
