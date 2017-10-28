package com.feldman.blazej.presenter;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.dct.DCT;
import com.feldman.blazej.helper.DocumentReader;
import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import com.feldman.blazej.model.Watermark;
import com.feldman.blazej.qrCode.QRCoder;
import com.feldman.blazej.services.DocumentService;
import com.feldman.blazej.util.AuthorizationUtils;
import com.feldman.blazej.util.HashCoder;
import com.feldman.blazej.util.ImageCreator;
import com.feldman.blazej.util.JpegWriter;
import com.feldman.blazej.util.RandomStringGenerator;
import com.google.zxing.NotFoundException;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static com.feldman.blazej.util.AuthorizationUtils.getUsernameFromSession;

/**
 * Created by Błażej on 02.12.2016.
 */
@UIScope
@Component
public class DocumentPresenter {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;
    @Autowired
    private WatermarkPresenter watermarkPresenter;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private UserPresenter userPresenter;
    @Autowired
    private DocumentReader documentReader;
    @Autowired
    private ImageCreator imageCreator;
    @Autowired
    private DCT dct;

    public void addNewDocument(Document document) {
        documentService.addDocument(document);
    }

    public void updateThisDocument(Document document) {
        documentService.updateDocument(document);
    }

    public Document searchDocumentByNameAndUser(String name,User user){
        return documentService.findByNameAndUserId(name, user);
    }


    public Document searchDocumentByName(String name) {
        return documentService.findByName(name);
    }

    public List<Document> searchDocumentsByUser(User user) {
        return documentService.findAllByUserId(user);
    }
    public int getLastIndexByUser(User user){
        List<Document> list = documentService.findAllByUserId(user);
        return list.size();
    }

    public List<Document> getDocumentList() {
        return documentService.getAllDocuments();

    }

    public Document searchDocumentByHashCode(String hashCode) {
        return documentService.findByDocHashCode(hashCode);
    }

    public String showDocument(Upload.SucceededEvent event) throws Exception {
        File file = new File("C:\\" + applicationConfiguration.getFilepath() + "\\" + event.getFilename());
        Watermark watermark;
        QRCoder qrCodeReader = new QRCoder();
        XWPFDocument wordDoc = null;

        if(searchDocumentByHashCode(HashCoder.getMD5Checksum(file.toString())) != null){

            watermark = watermarkPresenter.searchWatermarkByDocument(searchDocumentByHashCode(HashCoder.getMD5Checksum(file.toString())));
            if (watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession()))) {
                Notification.show("Nie posiadasz wystarczających uprawnień do odczytu.%$Zaloguj się na swoim koncie!");
                return "";
            } else{
                return "Dokument nie został naruszony i jest autentyczny.%$"+ watermark.getWatermarkText();
            }
        }
        try {
            wordDoc = (XWPFDocument) documentReader.readDocument(applicationConfiguration.getFilepath()+event.getFilename());
        }catch (NotFoundException e){
            e.printStackTrace();
            return "Nie znaleziono dokumentu";
        }
        for (XWPFParagraph p : wordDoc.getParagraphs()) {
            for (XWPFRun run : p.getRuns()) {
                for (XWPFPicture pic : run.getEmbeddedPictures()) {
                    byte[] img = pic.getPictureData().getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(img);
                    BufferedImage image = ImageIO.read(bais);
                    try {
                        ImageIO.write(image, "png", new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.png"));

                        watermark = watermarkPresenter.searchWatermarkByHash(qrCodeReader.readQRCode(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.png", new HashMap()));
                        if(watermark!=null) {
                            if (watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession()))) {
                                return "Nie posiadasz wystarczających uprawnień do odczytu.%$Zaloguj się na swoim koncie!";
                            } else {
                                return "Dokument został naruszony!%$" + watermark.getWatermarkText();
                            }
                        }

                    }catch(NotFoundException e){}


                }
                for (XWPFPicture pic : run.getEmbeddedPictures()) {
                    byte[] img = pic.getPictureData().getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(img);
                    BufferedImage image = ImageIO.read(bais);
                    JpegWriter.createPicture(new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead2.jpg"),"jpg",image);
                    try {
                        watermark = watermarkPresenter.searchWatermarkWith(dct.getDctParam(new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead2.jpg")));
                        if(watermark!=null) {
                            if (watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession()))) {
                                return "Nie posiadasz wystarczających uprawnień do odczytu.%$Zaloguj się na swoim koncie!";
                            } else {
                                return "Dokument został naruszony!%$"+ watermark.getWatermarkText();
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
            return "Dokument nie został wcześniej zabezpieczony ";
    }

    public Document saveNewDocument(Upload.SucceededEvent event, String protection,boolean watermark) throws Exception {
        String a = event.getFilename();
        int b = 1;
        while(searchDocumentByName(a)!=null){
            if(b==1) {
                a = a.substring(0, a.length() - 5) + b + ".docx";
            }else{
                a = a.substring(0, a.length() - 6) + b + ".docx";
            }
            b++;
        }
        XWPFDocument xwpfDocument = (XWPFDocument) documentReader.readDocument(applicationConfiguration.getFilepath() + event.getFilename());
        watermarkPresenter.setQrCodeHash(RandomStringGenerator.generateRandomValue());
        watermarkPresenter.setIndexNumber(getLastIndexByUser(userPresenter.searchUserByLogin(AuthorizationUtils.getUsernameFromSession())));
        imageCreator.addImageToXwpf(xwpfDocument,watermark);
        Document document = new Document();
        document.setDocumentId(null);
        document.setUserId(userPresenter.searchUserByLogin(getUsernameFromSession()));
        document.setName(a);
        File file = new File(applicationConfiguration.docFilePath + "\\qrcode.docx");
        if(watermark==true){
            XWPFDocument wordDoc = (XWPFDocument) documentReader.readDocument(applicationConfiguration.docFilePath + "\\qrcode.docx");
            for (XWPFParagraph p : wordDoc.getParagraphs()) {
                for (XWPFRun run : p.getRuns()) {
                    for (XWPFPicture pic : run.getEmbeddedPictures()) {
                            byte[] img = pic.getPictureData().getData();
                            ByteArrayInputStream bais = new ByteArrayInputStream(img);
                            BufferedImage image = ImageIO.read(bais);
                            JpegWriter.createPicture(new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.jpg"),"jpg",image);
                            try {
                                watermarkPresenter.WatermarkCode = dct.getDctParam(new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.jpg"));

                            } catch (Exception e) {
                                System.err.println("Problem z odczytem!!!!!!!");
                                e.printStackTrace();
                            }
                    }
                }
            }
        }
        document.setContent(createBytesString(file));
        document.setProtection(protection);
        String hash = HashCoder.getMD5Checksum(file.toString());
        document.setDocHashCode(hash);
        addNewDocument(document);
        return document;
    }

    public byte[] createBytesString(File file) throws IOException {
        Path path = Paths.get(file.toString());
        return Files.readAllBytes(path);
    }

    public void createFileFromByte(byte[] a,String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.write(a);
        fos.flush();
        fos.close();

    }
}
