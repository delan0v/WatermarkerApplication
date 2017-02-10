package com.feldman.blazej.repository;

import com.feldman.blazej.BaseSpringDatabseConfig;
import com.feldman.blazej.model.Watermark;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Created by Błażej on 30.11.2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WatermarkRepositoryTest extends  BaseSpringDatabseConfig implements RepositoryCrudTest {

    @Autowired
    private WatermarkRepository watermarkRepository;

    private Watermark watermark;

    @Before
    public void setUp() throws Exception {
        watermark = TestDataProviderUtil.newTestWatermark();
        assertNotNull(watermark);
        assertNotNull(watermarkRepository);
    }

    @Test
    @Override
    public void a_insertTest(){
        Watermark saveWatermark = watermarkRepository.save(watermark);
        assertNotNull(saveWatermark.getWatermarkId());
    }

    @Test
    @Override
    public void b_readTest() {
        List<Watermark>watermarkList = watermarkRepository.findAll();
        assertNotNull(watermarkList);
        assertFalse(watermarkList.isEmpty());
    }

    @Test
    @Override
    public void c_updateTest() {
        List<Watermark> watermarksList = watermarkRepository.findAll();
        Watermark watermarkToUpdate = watermarksList.get(watermarksList.size() - 1);
        assertNotNull(watermarkToUpdate);
        assertWatermark(watermarkToUpdate);

        watermarkToUpdate.setWatermarkBytes(new byte[]{(byte) 3212});
        watermarkRepository.save(watermarkToUpdate);

        Watermark udpateWatermark = watermarkRepository.findByWatermarkId(watermarkToUpdate.getWatermarkId());

        assertTrue(Arrays.equals(udpateWatermark.getWatermarkBytes(), new byte[]{(byte) 3212}));
    }


    @Test
    @Override
    public void d_deleteTest() {
        List<Watermark> watermarkList = watermarkRepository.findAll();
        Watermark watermarkToDelete = watermarkList.get(watermarkList.size() - 1);
        assertNotNull(watermarkToDelete);

        watermarkRepository.delete(watermarkToDelete);
        Watermark deletedWatermark = watermarkRepository.findByWatermarkId(watermarkToDelete.getWatermarkId());
        assertNull(deletedWatermark);
    }

    private void assertWatermark(Watermark watermarkToUpdate) {
        assertEquals(watermark.getDocument(), TestDataProviderUtil.newTestDocument());
        assertTrue(Arrays.equals(watermark.getWatermarkBytes(), new byte[]{(byte) 2313}));
    }
}
