package ru.tpu.russian.back.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.Base64;

@Service
public class MediaService {

//    private MediaRepository mediaRepository;
//
//    public MediaService(MediaRepository mediaRepository) {
//        this.mediaRepository = mediaRepository;
//    }

    public String getImage(String id) throws IOException {
        File img = new File("C:/test.jpg");
        BufferedImage bufferedImage = ImageIO.read(img);
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return Base64.getEncoder().encodeToString(data.getData());
    }

    public byte[] getTestImage() throws IOException {
        File img = new File("C:/test.jpg");
        BufferedImage bufferedImage = ImageIO.read(img);
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return data.getData();
    }

}
