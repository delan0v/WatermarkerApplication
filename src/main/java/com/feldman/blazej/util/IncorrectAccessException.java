package com.feldman.blazej.util;

import com.vaadin.ui.Notification;

/**
 * Created by Błażej on 28.04.2017.
 */
public class IncorrectAccessException extends Exception {
    public IncorrectAccessException(){
        Notification.show("Aby odczytać ten dokument musisz sie zalogować");
    }
}
