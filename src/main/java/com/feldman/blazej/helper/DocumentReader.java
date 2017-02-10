package com.feldman.blazej.helper;

import com.feldman.blazej.configuration.ApplicationConfiguration;
import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.repository.WatermarkRepository;
import com.feldman.blazej.util.FileUtils;
import com.feldman.blazej.util.ImageCreator;
import com.feldman.blazej.util.IncorrectFormatException;
import com.feldman.blazej.util.StringGenerator;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.apache.poi.POIDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocumentReader {

    private static final Logger logger = LoggerFactory.getLogger(DocumentReader.class);

    @Autowired
    private ApplicationConfiguration configuration;



    /**
     * Metoda parsuje plik doc/docx i zwraca odczytany z dokumentu tekst
     *
     * @param filename - nazwa pliku do parsowania
     * @return tekst pobrany z dokuemntu
     */
    public Object readDocument(String filename) throws IncorrectFormatException, WriterException, InvalidFormatException, NotFoundException, IOException {

        File file = new File(configuration.getFilepath() + filename);
        String fileExtension = FileUtils.getFileExtension(filename);
        if ("docx".equalsIgnoreCase(fileExtension)) {
            return processDocxDocument(file);
        } else if ("doc".equalsIgnoreCase(fileExtension)) {
            return processDocDocument(file);
        } else {
            logger.error("Niepoprawne rozszerzenie pliku {} ! Nie powinno nigdy do tego dojść ", filename);
            throw new IncorrectFormatException();
        }
    }

    private XWPFDocument processDocxDocument(File file) throws WriterException, InvalidFormatException, NotFoundException, IOException {

        XWPFDocument document;
        try {
            FileInputStream fis = new FileInputStream(file.getAbsoluteFile());
            document = new XWPFDocument(fis);
        } catch (FileNotFoundException e) {
            logger.error("Nie można odnaleźć pliku", e);
            return null;
        } catch (IOException e) {
            logger.error("Problem z wczytaniem pliku", e);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        StringGenerator stringGenerator = new StringGenerator(32);

        return document;
    }


    private String processDocDocument(File file) {
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument hwpfDocument = new HWPFDocument(fis);
            WordExtractor wordExtractor = new WordExtractor(hwpfDocument);
            logger.debug(wordExtractor.getText());
            return wordExtractor.getText();
        } catch (FileNotFoundException e) {
            logger.error("Nie można odnaleźć pliku", e);
        } catch (IOException e) {
            logger.error("Problem z wczytaniem pliku", e);
        }
        return null;
    }
}
