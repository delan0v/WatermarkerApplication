package com.feldman.blazej.view.userInterface;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.model.Logo;
import com.feldman.blazej.presenter.LogoPresenter;
import com.feldman.blazej.presenter.UserPresenter;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.view.common.ViewNames;
import com.feldman.blazej.view.component.DocumentReceiver;
import com.feldman.blazej.view.component.WUploadPanel;
import com.feldman.blazej.view.upload.AccountView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
@SpringView(name = ViewNames.WATERMARK_UPLOAD_VIEW)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WatermarkUploadView extends VerticalLayout implements View, Upload.StartedListener, Upload.SucceededListener, Upload.FailedListener {

        private static final Logger logger = LoggerFactory.getLogger(AccountView.class);

        private Upload upload;
        @Autowired
        public ApplicationConfiguration configuration;
        @Autowired
        public UserPresenter userPresenter;
        @Autowired
        public LogoPresenter logoPresenter;

        @PostConstruct
        private void init() {
            setSizeFull();
            setMargin(true);
            setSpacing(true);

            WUploadPanel uploadPanel2 = new WUploadPanel("Dodaj znak w formacie .jpg posiadający rozmiar 100x100px", new DocumentReceiver(configuration.getFilepath()));

            upload = uploadPanel2.getUpload();
            upload.addFailedListener(this);
            upload.addSucceededListener(this);
            upload.addStartedListener(this);

            addComponent(uploadPanel2);
            setComponentAlignment(uploadPanel2,Alignment.MIDDLE_CENTER);

        }

        @Override
        public void uploadFailed(Upload.FailedEvent failedEvent) {
                logger.warn("Upload dokumentu zakończony niepowodzeniem");
        }

        @Override
        public void uploadStarted(Upload.StartedEvent startedEvent) {

        }

        @Override
        public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
            logger.debug("Upload dokumentu zakończony");
            logger.debug("Procesowanie dokumentu... >>>");
            logger.debug("<<< Zakończono procesowanie dokumentu.");

            try {
                BufferedImage bufImgs = ImageIO.read(new File(configuration.getFilepath()+succeededEvent.getFilename()));
                if((bufImgs.getWidth()!=100)&&(bufImgs.getHeight()!=100)) {
                    upload.interruptUpload();
                    Notification.show("Logo ma zły rozmiar!");
                }else{

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufImgs, "jpg", baos);
                    byte[] pixels = baos.toByteArray();
                    baos.flush();
                    baos.close();

                    Logo logo = new Logo();
                    logo.setLogoId(null);
                    logo.setUserId(userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession()));
                    logo.setContent(pixels);
                    String logoName = succeededEvent.getFilename();
                    int i=1;
                    while(logoPresenter.getLogoFromName(logoName)!=null){
                        if(i==1){
                            logoName = logoName.substring(0, logoName.length() - 4) + i + ".jpg";
                         }else{
                            logoName = logoName.substring(0, logoName.length() - 5) + i + ".jpg";
                        }
                        i++;
                    }
                    logo.setName(logoName);
                    logoPresenter.addNewLogo(logo);
                    Notification.show("Udane pobranie loga");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Notification.show("Pobranie loga nie powiodło się");
            }

        }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}