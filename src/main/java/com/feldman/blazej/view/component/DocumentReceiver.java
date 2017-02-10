package com.feldman.blazej.view.component;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class DocumentReceiver implements Upload.Receiver {

    private String filepath;

    public DocumentReceiver(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(new File(filepath + filename));
        } catch (FileNotFoundException e) {
            new Notification("Nie można otworzyć pliku ", e.getMessage(), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
        }
        return fos;
    }
}
