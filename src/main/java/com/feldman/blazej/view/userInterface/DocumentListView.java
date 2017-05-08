package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.model.Document;
import com.feldman.blazej.presenter.DocumentPresenter;
import com.feldman.blazej.presenter.UserPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.print.Doc;
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


    public DocumentListView() {
        setSizeFull();
        setMargin(true);
    }

    @PostConstruct
    public void init(){

        List<Document> docList = documentPresenter.searchDocumentsByUser(userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession()));
        List<String> nameList = new ArrayList<>();
        for(Document document:docList){
            nameList.add(document.getName());
        }
        ComboBox comboBox = new ComboBox("Wybierz dokument",nameList);
        addComponent(comboBox);
        setComponentAlignment(comboBox, Alignment.MIDDLE_CENTER);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
