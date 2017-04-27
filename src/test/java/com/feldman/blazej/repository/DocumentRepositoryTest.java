package com.feldman.blazej.repository;

import com.feldman.blazej.BaseSpringDatabseConfig;
import com.feldman.blazej.model.Document;
import com.feldman.blazej.model.User;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentRepositoryTest extends BaseSpringDatabseConfig implements RepositoryCrudTest {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    private Document document;

    @Before
    public void setUp() throws Exception {
        document = TestDataProviderUtil.newTestDocument();
        assertNotNull(documentRepository);
        assertNotNull(document);
    }

    @Test
    @Override
    public void a_insertTest() {
        Document saveDocment = documentRepository.save(document);
        assertNotNull(saveDocment.getDocumentId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void aa_insert_withNullUserTest() {
        document.setUserId(null);
        Document saveDocment = documentRepository.save(document);
        assertNotNull(saveDocment.getDocumentId());
    }

    @Test
    @Override
    public void b_readTest() {
        List<Document> documentList = documentRepository.findAll();
        assertNotNull(documentList);
        assertFalse(documentList.isEmpty());
    }

    @Test
    @Override
    public void c_updateTest() {
        List<Document> documentList = documentRepository.findAll();
        // documentList.size() - 1 - pobieram ostatni dodany dokument z listy pobranych
        Document documentToUpdate = documentList.get(documentList.size() - 1);
        assertNotNull(documentToUpdate);
        assertDocument(documentToUpdate);

        User user = TestDataProviderUtil.newTestUser();
        user = userRepository.save(user);
        documentToUpdate.setUserId(user);
        //documentToUpdate.setContent("newDocumentsContent");
        documentToUpdate.setName("newDocument");
        documentRepository.save(documentToUpdate);

        Document udpateDocument = documentRepository.findOne(documentToUpdate.getDocumentId());
        assertEquals(udpateDocument.getContent(), "newDocumentsContent");
        assertEquals(udpateDocument.getName(), "newDocument");
    }


    @Test
    @Override
    public void d_deleteTest() {
        List<Document> documentList = documentRepository.findAll();
        Document documentToDelete = documentList.get(0);
        assertNotNull(documentToDelete);

        documentRepository.delete(documentToDelete);
        Document deletedDocument = documentRepository.findOne(documentToDelete.getDocumentId());
        assertNull(deletedDocument);
    }

    private void assertDocument(Document document) {
        assertEquals(document.getName(), "documentName");
        assertEquals(document.getContent(), "documentsContent");
    }

}