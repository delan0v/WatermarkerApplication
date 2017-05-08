package com.feldman.blazej.util;

import com.vaadin.server.VaadinSession;

public class AuthorizationUtils {

    public static final String SESSION_USERNAME_KEY = "LOGGED_USER";


    public static void saveUsernameInSession(String userLogin) {
        VaadinSession.getCurrent().setAttribute(SESSION_USERNAME_KEY, userLogin);
    }

    public static String getUsernameFromSession() {
        try {
            return VaadinSession.getCurrent().getAttribute(SESSION_USERNAME_KEY).toString();
        } catch (Exception e) {
            return null;
        }
    }
}
