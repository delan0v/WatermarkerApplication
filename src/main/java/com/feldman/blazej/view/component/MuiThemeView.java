package com.feldman.blazej.view.component;

import com.feldman.blazej.view.common.ViewNames;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Błażej on 25.03.2017.
 */
@SpringView(name = ViewNames.LOGIN_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MuiThemeView extends VerticalLayout implements View {

    public MuiThemeView(){
        setSizeFull();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.setHeight("24px");

        Image image = new Image();
        image.setSource(new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/WEB-INF/img/herbWAT.png")));
        image.setSizeUndefined();

        Image image1 = new Image();
        image1.setSource(new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/WEB-INF/img/godlo.png")));
        image1.setSizeUndefined();

        Image image2 = new Image();
        image2.setSource(new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/WEB-INF/img/nazwa.png")));
        image2.setSizeUndefined();

        Image image3 = new Image();
        image3.setSource(new FileResource(new File(VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath()+"/WEB-INF/img/belka.png")));
        image3.setSizeUndefined();


        addComponent(horizontalLayout);
        addComponent(image3);
        horizontalLayout.addComponent(image);
        horizontalLayout.addComponent(image1);
        horizontalLayout.addComponent(image2);
        setComponentAlignment(horizontalLayout,Alignment.TOP_CENTER);
        setComponentAlignment(image3,Alignment.TOP_CENTER);
        horizontalLayout.setComponentAlignment(image,Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(image1,Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(image2,Alignment.MIDDLE_CENTER);


    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
