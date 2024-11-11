package com.example.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

public class MyMultipartFile implements MultipartFile {
    private final byte[] fileContent;
    private final String fileName;
    private final String contentType;

    public MyMultipartFile(byte[] fileContent, String fileName, String contentType) {
        this.fileContent = fileContent;
        this.fileName = fileName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException {
        java.nio.file.Files.write(dest.toPath(), fileContent);
    }

    public static MultipartFile fromBufferedImage(BufferedImage image, String format, String fileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        return new MyMultipartFile(imageBytes, fileName, "image/" + format);
    }
    
    public static MultipartFile fromURL(String imageUrl) throws IOException {
        WebClient webClient = WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
                .build();
        
        byte[] imageBytes = webClient.get()
                .uri(imageUrl)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        if (imageBytes == null) {
            throw new IOException("Failed to download image from URL");
        }

        // 从 URL 中提取文件名
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        String contentType = "image/png"; 

        return new MyMultipartFile(imageBytes, fileName, contentType);
    }
}
