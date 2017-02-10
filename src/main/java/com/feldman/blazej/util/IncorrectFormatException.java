package com.feldman.blazej.util;

import com.vaadin.ui.Notification;

/**
 * Created by Błażej on 05.12.2016.
 */
public class IncorrectFormatException extends Exception {

    public IncorrectFormatException() {
        Notification.show("Dokument posiada złe rozszerzenie!");
    }

    public IncorrectFormatException(String message) {
        super(message);
    }

    public IncorrectFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFormatException(Throwable cause) {
        super(cause);
    }
}
