package com.upload.image.service;

import com.upload.image.service.exception.DataIntegrityViolationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;


@Service
public class FileService {

    static final float MAX_COMPRESSION_SIZE = 600; //600kb

    public Path upload(MultipartFile file) throws IOException {
        Path path = null;
        String fileType = file.getContentType();
        String fileName = file.getOriginalFilename();
        byte[] imageFistBytes = file.getBytes();
        if (file.isEmpty()) {
            return null;
        }
        if (file.getContentType().equals("image/jpeg")) {
            byte[] compressedImageJpg = compressImageJpg(file.getBytes(), 1);
            path = saveImage(compressedImageJpg, fileType, fileName);
        } else if (file.getContentType().equals("image/png")) {
            byte[] compressedImagePng = compressImagePng(file.getBytes(), 1);
            path = saveImage(compressedImagePng, fileType, fileName);
        }
        return path;
    }

    public Path saveImage(byte[] bytes, String fileType, String fileName) throws IOException {
        Path path = null;
        String randomStr = RandomStringUtils.randomAlphabetic(20);
        String valideTypes = " image/jpeg, image/png";
        if (valideTypes.contains(fileType)) {
            path = Paths.get("file/" + randomStr + "_" + fileName);
            Files.write(path, bytes);
        } else {
            throw new DataIntegrityViolationException("File is not a supported type");
        }
        return path;
    }

    public static byte[] compressImageJpg(byte[] bytes, float imageQuality) throws IOException {
        float kbSize = bytes.length / 1024f ;

        if (kbSize <= MAX_COMPRESSION_SIZE || imageQuality <= 0) {
            return bytes;
        } else {

            //https://docs.oracle.com/javase/7/docs/api/java/io/ByteArrayOutputStream.html
            // data is written into a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Get image writers
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(
                    "jpg"); // Input your Format Name here

            if (!imageWriters.hasNext()) {
                throw new IllegalStateException("Writers Not Found!!");
            }
            //https://docs.oracle.com/javase/7/docs/api/javax/imageio/ImageWriter.html
            ImageWriter imageWriter = imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            // Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);

            // Create the buffered image
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            //Remove alpha channel to avoid (bogus input errors)
            //what is - https://www.makeuseof.com/tag/alpha-channel-images-mean/
            bufferedImage = removeAlphaChannel(bufferedImage);

            // Compress and insert the image into the byte array.
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

            // close all streams !important
            inputStream.close();
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();

            bytes = outputStream.toByteArray();
            System.out.println(bytes);

            return compressImageJpg(bytes, imageQuality - .05f);
        }
    }

    public static byte[] compressImagePng(byte[] bytes, float imageQuality) throws IOException {
        float kbSize = bytes.length / 1024f ;

        if (kbSize <= MAX_COMPRESSION_SIZE || imageQuality <= 0) {
            return bytes;
        } else {

            //https://docs.oracle.com/javase/7/docs/api/java/io/ByteArrayOutputStream.html
            // data is written into a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Get image writers
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(
                    "jpg"); // Input your Format Name here

            if (!imageWriters.hasNext()) {
                throw new IllegalStateException("Writers Not Found!!");
            }
            //https://docs.oracle.com/javase/7/docs/api/javax/imageio/ImageWriter.html
            ImageWriter imageWriter = imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            // Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);

            // Create the buffered image
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            //Remove alpha channel to avoid (bogus input errors)
            //what is - https://www.makeuseof.com/tag/alpha-channel-images-mean/
            bufferedImage = removeAlphaChannel(bufferedImage);

            // Compress and insert the image into the byte array.
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);

            // close all streams !important
            inputStream.close();
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();

            bytes = outputStream.toByteArray();
            System.out.println(bytes);

            return compressImagePng(bytes, imageQuality - .05f);
        }
    }


    private static BufferedImage removeAlphaChannel(BufferedImage img) {
        if (!img.getColorModel().hasAlpha()) {
            return img;
        }

        BufferedImage target = createImage(img.getWidth(), img.getHeight(), false);
        Graphics2D g = target.createGraphics();
        // g.setColor(new Color(color, false));
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return target;
    }

    private static BufferedImage createImage(int width, int height, boolean hasAlpha) {
        return new BufferedImage(width, height,
                hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
    }



}
