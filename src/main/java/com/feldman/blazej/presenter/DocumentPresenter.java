package com.feldman.blazej.presenter;

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

import static com.feldman.blazej.util.AuthorizationUtils.getUsernameFromSession;

/**
 * Created by Błażej on 02.12.2016.
 */
@UIScope
@Component
public class DocumentPresenter {

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

    public String showDocument(Upload.SucceededEvent event) throws IncorrectFormatException, WriterException, InvalidFormatException, NotFoundException, IOException{
        String showMe = "";
        QRCodeReader qrCodeReader = new QRCodeReader();
        Watermark watermark;
        XWPFDocument wordDoc = (XWPFDocument) documentReader.readDocument(event.getFilename());
        for (XWPFParagraph p : wordDoc.getParagraphs()) {
            for (XWPFRun run : p.getRuns()) {
                for (XWPFPicture pic : run.getEmbeddedPictures()) {
                    byte[] img = pic.getPictureData().getData();
                    ByteArrayInputStream bais = new ByteArrayInputStream(img);
                    BufferedImage image = ImageIO.read(bais);
                    ImageIO.write(image, "png", new File(ImageCreator.qrcFilePathShow + "qrcodeToRead.png"));
                    watermark = watermarkPresenter.searchWatermarkById(qrCodeReader.readQRCode(ImageCreator.qrcFilePathShow + "qrcodeToRead.png", new HashMap()));
                    if ((watermark.getDocument().getProtection().equals("Prywatny") && !(watermark.getDocument().getUserId().getUserLogin().equals(getUsernameFromSession())))) {
                        Notification.show("Nie posiadasz wystarczających uprawnień do odczytu. Zaloguj się na swoim koncie!");
                        return "";
                    } else {
                        showMe = showMe + watermark.getWatermarkText();
                    }
                }
            }
        }
        return showMe;
    }


    public Document saveNewDocument(Upload.SucceededEvent event, File file, String protection) throws IOException, NoSuchAlgorithmException, IncorrectFormatException, WriterException, InvalidFormatException, NotFoundException {

        watermarkPresenter.setQrCodeHash(HashCoder.getMd5Hash(createBytesString(file)));
        XWPFDocument xwpfDocument = (XWPFDocument) documentReader.readDocument(event.getFilename());
        imageCreator.addImageToXwpf(xwpfDocument);
        Document document = new Document();
        document.setDocumentId(null);
        document.setUserId(userPresenter.searchUserByLogin(getUsernameFromSession()));
        document.setName(event.getFilename());
        document.setContent(createBytesString(file));
        document.setDocHashCode(watermarkPresenter.getQrCodeHash());
        document.setProtection(protection);
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
