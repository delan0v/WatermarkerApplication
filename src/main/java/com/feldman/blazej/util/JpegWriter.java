package com.feldman.blazej.util;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JpegWriter {

    public static void createPicture(File file, String format, BufferedImage bufChange) {


        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(1f);

        final ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
        try {
            writer.setOutput(new FileImageOutputStream(file));
            writer.write(null, new IIOImage(bufChange, null, null), jpegParams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
