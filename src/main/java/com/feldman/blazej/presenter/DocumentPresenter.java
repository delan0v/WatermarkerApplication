package com.feldman.blazej.presenter;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.dct.DCT;
import com.feldman.blazej.helper.DocumentReader;
import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import com.feldman.blazej.model.Watermark;
import com.feldman.blazej.qrCode.QRCodeReader;
import com.feldman.blazej.services.DocumentService;
import com.feldman.blazej.util.HashCoder;
import com.feldman.blazej.util.ImageCreator;
import com.feldman.blazej.util.IncorrectAccessException;
import com.feldman.blazej.util.IncorrectFormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.print.Doc;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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

    public List<Document> getDocumentList() {
        return documentService.getAllDocuments();

    }

    public Document searchDocumentByHashCode(String hashCode) {
        return documentService.findByDocHashCode(hashCode);
    }

    public String showDocument(Upload.SucceededEvent event) throws Exception {
        File file = new File("C:\\" + applicationConfiguration.getFilepath() + "\\" + event.getFilename());
        String showMe = "";
        Watermark watermark;
        QRCodeReader qrCodeReader = new QRCodeReader();
        XWPFDocument wordDoc = null;
        DCT dct;
        if(searchDocumentByHashCode(HashCoder.getMD5Checksum(file.toString())) != null){

            watermark = watermarkPresenter.searchWatermarkByDocument(searchDocumentByHashCode(HashCoder.getMD5Checksum(file.toString())));
            if (watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession()))) {
                Notification.show("Nie posiadasz wystarczających uprawnień do odczytu. Zaloguj się na swoim koncie!");
                return "";
            } else{
                return "Dokument nie został naruszony i jest autentyczny: "+ watermark.getWatermarkText();
            }
        }
        try {
            wordDoc = (XWPFDocument) documentReader.readDocument(event.getFilename());
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
                        watermark = watermarkPresenter.searchWatermarkById(qrCodeReader.readQRCode(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.png", new HashMap()));
                        if (watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession()))) {
                            Notification.show("Nie posiadasz wystarczających uprawnień do odczytu. Zaloguj się na swoim koncie!");
                            return "";
                        } else {
                            showMe = watermark.getWatermarkText();
                        }

                    }catch(NotFoundException e){}

                }
                for (XWPFPicture pic : run.getEmbeddedPictures()) {
                    byte[] img = pic.getPictureData().getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(img);
                    BufferedImage image = ImageIO.read(bais);

                    ImageIO.write(image, "jpg", new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.jpg"));
                    dct = new DCT();
                    try {
                        dct.loadPicture(new File(applicationConfiguration.qrcFilePathShow + "qrcodeToRead.jpg"));
                        dct.goneDCT();
                        watermark = watermarkPresenter.searchWatermarkWith(DCT.watermarkParam);
                        if(watermark!=null) {
                            if (watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession()))) {
                                Notification.show("Nie posiadasz wystarczających uprawnień do odczytu. Zaloguj się na swoim koncie!");
                                return "";
                            } else {
                                showMe = watermark.getWatermarkText();
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
        if(showMe.equals("")){
            return "Dokument nie został wcześniej zabezpieczony";
        }
        return showMe;
    }

    public Document saveNewDocument(Upload.SucceededEvent event, File file, String protection,boolean watermark) throws Exception {

        XWPFDocument xwpfDocument = (XWPFDocument) documentReader.readDocument(event.getFilename());
        imageCreator.addImageToXwpf(xwpfDocument,watermark);
        Document document = new Document();
        document.setDocumentId(null);
        document.setUserId(userPresenter.searchUserByLogin(getUsernameFromSession()));
        document.setName(event.getFilename());
        document.setContent(createBytesString(file));
        document.setProtection(protection);
        File file2 = new File(applicationConfiguration.docFilePath + "\\qrcode.docx");
        String hash = HashCoder.getMD5Checksum(file2.toString());
        watermarkPresenter.setQrCodeHash(hash);
        document.setDocHashCode(hash);
        addNewDocument(document);
        return document;
    }

    public byte[] createBytesString(File file) throws UnsupportedEncodingException {
        byte [] b = new byte[(int) file.length()-1];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public void createFileFromByte(byte[] a) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(a);
        out.close();
        bos.close();


    }
}
