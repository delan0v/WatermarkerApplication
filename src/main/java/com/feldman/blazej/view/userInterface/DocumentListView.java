package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.presenter.DocumentPresenter;
import com.feldman.blazej.presenter.UserPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Błażej on 29.04.2017.
 */
@Component
@SpringView(name = ViewNames.DOCUMENT_LIST_VIEW)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentListView extends VerticalLayout implements View {

    @Autowired
    private DocumentPresenter documentPresenter;

    @Autowired
    private UserPresenter userPresenter;
    private ComboBox comboBox;
    private List<String> nameList;
    private Button downloadButton;


    public DocumentListView() {
        setSizeFull();
        setMargin(true);

    }
    @PostConstruct
    public void init(){
        refresh();
    }
    public void refresh(){

        removeAllComponents();
        nameList = new ArrayList<>();
        comboBox = new ComboBox("Wybierz dokument",nameList);
        comboBox.setWidth("250");

        downloadButton = new Button("Pobierz plik");
        downloadButton.setWidth("250");
        downloadButton.addClickListener((Button.ClickListener)event-> {
            try {
                File home = FileSystemView.getFileSystemView().getHomeDirectory();
                documentPresenter.createFileFromByte(documentPresenter.searchDocumentByNameAndUser(comboBox.getValue().toString(), userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession())).getContent(),home.getAbsolutePath()+"\\"+comboBox.getValue().toString());
                Notification.show("Udane pobranie dokumentu");
            } catch (IOException e) {
                Notification.show("Wystąpił błąd, spróbuj ponownie");
                e.printStackTrace();
            }
        });

        List<Document> docList = documentPresenter.searchDocumentsByUser(userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession()));
        for(Document document:docList){
            nameList.add(document.getName());
        }
        comboBox = new ComboBox("Wybierz dokument",nameList);
        addComponent(comboBox);
        addComponent(downloadButton);
        setComponentAlignment(comboBox, Alignment.MIDDLE_CENTER);
        setComponentAlignment(downloadButton, Alignment.MIDDLE_CENTER);

        addComponent(comboBox);
        addComponent(downloadButton);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
