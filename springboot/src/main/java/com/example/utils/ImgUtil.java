package com.example.utils;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.Result;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ImgUtil {
    /**
     * Convert MultipartFile to BufferedImage
     * @param file MultipartFile
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage MultipartFileToBufferedImage(MultipartFile file) throws IOException{
        return ImageIO.read(file.getInputStream());
    }

    /**
     * Convert BufferedImage To MutipartFile
     * @param img BufferedImage
     * @param fileName filename of MultipartFile
     * @return MultipartFile
     * @throws IOException
     */
    public static MultipartFile BufferedImageToMultipartFile(BufferedImage img, String fileName) throws IOException {
        return MyMultipartFile.fromBufferedImage(img, "png", fileName); 
    }

    /**
     * 对图片进行加密
     * @param file 原图像文件
     * @return 加密后的图像文件
     */
    public static BufferedImage ImageEncryptor(BufferedImage img, String privateKey) throws Exception {
        if (privateKey == null || privateKey.isEmpty() || privateKey.length() < 5) {
            throw new Exception("非法私钥");
        }
    
        int u = 0;
        for (int i = 0; i < 5; i++) {
            u += privateKey.charAt(i);
        }
    
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random(u);
    
        int[][][] d = new int[h][w][3];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int k = 0; k < 3; k++) {
                    d[i][j][k] = rand.nextInt(511) - 255; // [-255, 255]
                }
            }
        }
    
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int rgb = img.getRGB(j, i);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
    
                int newRed = Math.min(255, Math.max(0, red + d[i][j][0]));
                int newGreen = Math.min(255, Math.max(0, green + d[i][j][1]));
                int newBlue = Math.min(255, Math.max(0, blue + d[i][j][2]));
    
                int newRGB = (newRed << 16) | (newGreen << 8) | newBlue;
                out.setRGB(j, i, newRGB);
            }
        }
        return out;
    }
    
    
    /**
     * 图像的解密
     * @param img 加密过的图像
     * @return 解密后的图像
     */
    public static BufferedImage ImageDecryptor(BufferedImage img, String privateKey) throws Exception {
        if (privateKey == null || privateKey.isEmpty() || privateKey.length() < 5) {
            throw new Exception("非法私钥");
        }
    
        int u = 0;
        for (int i = 0; i < 5; i++) {
            u += privateKey.charAt(i);
        }
    
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random(u);
    
        int[][][] d = new int[h][w][3];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int k = 0; k < 3; k++) {
                    d[i][j][k] = rand.nextInt(511) - 255; // [-255, 255]
                }
            }
        }
    
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int encryptedRGB = img.getRGB(j, i);
                int encryptedRed = (encryptedRGB >> 16) & 0xFF;
                int encryptedGreen = (encryptedRGB >> 8) & 0xFF;
                int encryptedBlue = encryptedRGB & 0xFF;
    
                int decryptedRed = Math.min(255, Math.max(0, (encryptedRed - d[i][j][0] + 255) % 255));
                int decryptedGreen = Math.min(255, Math.max(0, (encryptedGreen - d[i][j][1] + 255) % 255));
                int decryptedBlue = Math.min(255, Math.max(0, (encryptedBlue - d[i][j][2] + 255) % 255));
    
                int decryptedRGB = (decryptedRed << 16) | (decryptedGreen << 8) | decryptedBlue;
                out.setRGB(j, i, decryptedRGB);
            }
        }
        return out;
    }
    

    /**
     * Convert BufferedImage base64
     * @param img BufferedImage
     * @return string of base64
     */
    public static String getImageBase64(BufferedImage img) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", byteArrayOutputStream); 
            byte[] imgBytes = byteArrayOutputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imgBytes);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert base64 string to BufferedImage
     * @param base64String base64 encoded string of an image
     * @return BufferedImage
     */
    public static BufferedImage base64ToImage(String base64String) {
        try {
            // 解码 Base64 字符串为字节数组
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            // 将字节数组转换为 ByteArrayInputStream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
            // 使用 ImageIO 读取输入流并转换为 BufferedImage
            return ImageIO.read(inputStream);
        } catch (IOException | IllegalArgumentException e) {
            // 处理可能的异常，如输入的 Base64 字符串格式不正确或解码过程出错
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 生成二维码，数据是 seed
     * @param seed
     * @param baseFilePath
     * @param ip
     * @param port
     * @return 二维码的 url
     */
    public static String generateQR(String seed, String baseFilePath, String ip, String port) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置错误校正级别为 Q（25%）
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        // 设置边距为 0
        hints.put(EncodeHintType.MARGIN, 0);
        String fileName = System.currentTimeMillis() + "-blockAddrQR.png";
        String fullFilePath = baseFilePath + fileName;
        try {
            // 增加二维码的尺寸为 800x800
            BitMatrix bitMatrix = qrCodeWriter.encode(seed, BarcodeFormat.QR_CODE, 800, 800, hints);
            File file = new File(fullFilePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "png", file.toPath());
            return "http://" + ip + ":" + port + "/files/" + fileName;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析二维码
     * @param file
     * @return 二维码的内容
     */
    public static String parseQR(MultipartFile file) {
        try {
            // 将上传的文件转换为 BufferedImage
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            // 使用 ZXing 解析二维码
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

            // 创建二维码解析器
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap);

            // 返回二维码内容
            return result.getText();
        } catch (Exception e) {
            // 处理异常情况，例如文件不是二维码或解析失败
            e.printStackTrace();
            return null;
        }
    }
}
