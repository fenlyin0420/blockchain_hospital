package com.example.utils;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class ImgUtil {
    /**
     * 对图片进行加密
     * @param file 原图像文件
     * @return 加密后的图像文件
     */
    public static MultipartFile ImageEncryptor(MultipartFile file) {
        // encryption key
        int u = 107;
        try {
            // 转为 BufferedImage, 便于处理
            BufferedImage img = ImageIO.read(file.getInputStream());
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 初始化随机扰动数组
            int[][][] d = new int[h][w][3]; // [高度][宽度][RGB 三个通道]
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    // 使用 u, i, j 作为种子来生成随机数
                    Random rand = new Random(u * i * j);
                    for (int k = 0; k < 3; k++) {
                        d[i][j][k] = rand.nextInt(511) - 255; // 生成范围 [-255, 255]
                    }
                }
            }

            // 应用扰动并保存新图像
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int rgb = img.getRGB(j, i);
                    // 分别获取 R, G, B 三个通道的值
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;
                    
                    // 对每个通道应用随机扰动
                    int newRed = (red + d[i][j][0]) % 255;
                    int newGreen = (green + d[i][j][1]) % 255;
                    int newBlue = (blue + d[i][j][2]) % 255;

                    // 确保像素值为非负
                    if (newRed < 0) newRed += 255;
                    if (newGreen < 0) newGreen += 255;
                    if (newBlue < 0) newBlue += 255;

                    // 将加密后的 R, G, B 组合回一个 RGB 值
                    int newRGB = (newRed << 16) | (newGreen << 8) | newBlue;
                    out.setRGB(j, i, newRGB);
                }
            }
            return MyMultipartFile.fromBufferedImage(out, "png", file.getOriginalFilename());
        } catch (IOException e) {
            System.err.println("出错啦：（: " + e.getMessage());
            return file;
        }
    }
    
    /**
     * 图像的解密
     * @param file 加密过的图像
     * @return 解密后的图像
     */
    public static MultipartFile ImageDecryptor(MultipartFile file) {
        // key
        int u = 107;

        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            // 初始化随机扰动数组
            int[][][] d = new int[h][w][3]; // [高度][宽度][RGB 三个通道]
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    // 使用 u, i, j 作为种子来生成随机数
                    Random rand = new Random(u * i * j);
                    for (int k = 0; k < 3; k++) {
                        d[i][j][k] = rand.nextInt(511) - 255; // 生成范围 [-255, 255]
                    }
                }
            }

            // 应用解密操作并保存新图像
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int encryptedRGB = img.getRGB(j, i);
                    // 分别获取加密后的 R, G, B 三个通道的值
                    int encryptedRed = (encryptedRGB >> 16) & 0xFF;
                    int encryptedGreen = (encryptedRGB >> 8) & 0xFF;
                    int encryptedBlue = encryptedRGB & 0xFF;
                    
                    // 对每个通道应用解密操作
                    int decryptedRed = (encryptedRed - d[i][j][0]) % 255;
                    int decryptedGreen = (encryptedGreen - d[i][j][1]) % 255;
                    int decryptedBlue = (encryptedBlue - d[i][j][2]) % 255;

                    // 确保解密后的像素值为非负
                    if (decryptedRed < 0) decryptedRed += 255;
                    if (decryptedGreen < 0) decryptedGreen += 255;
                    if (decryptedBlue < 0) decryptedBlue += 255;

                    // 将解密后的 R, G, B 组合回一个 RGB 值
                    int decryptedRGB = (decryptedRed << 16) | (decryptedGreen << 8) | decryptedBlue;
                    out.setRGB(j, i, decryptedRGB);
                }
            }
            return MyMultipartFile.fromBufferedImage(out, "png", file.getOriginalFilename());

        } catch (IOException e) {
            System.err.println("解密失败呜呜呜：（" + e.getMessage());
            return file;
        }
    }
}
