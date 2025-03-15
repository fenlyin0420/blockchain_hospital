package com.example.service;

import java.io.*;
import java.net.URLEncoder;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.controller.FileController;
import com.example.utils.ImgUtil;
import com.example.exception.CustomException;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.thread.ThreadUtil;

@Service
public class FileService {
    // 文件上传存储路径，为服务器本地文件系统路径
    // e.g. D:\Projects\Web\blockchain_hospital\Project/files/
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${server.port:9090}")
    private String port;

    @Value("${ip:localhost}")
    private String ip;

    public String save(MultipartFile file) {
        //获取当前时间戳
        String timeStamp;
        synchronized (FileController.class) {
            timeStamp = System.currentTimeMillis() + "";
            ThreadUtil.sleep(1L);
        }
        //获取文件名
        String fileName = file.getOriginalFilename();
        try {
            //如果没 files 文件夹，那么在当前根目录下创建一个file
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            // 文件存储形式：时间戳-文件名
            fileName = timeStamp + "-" + fileName;
            FileUtil.writeBytes(file.getBytes(), filePath + fileName);  // ***/manager/files/1697438073596-avatar.png
        } catch (Exception e) {
            System.err.println(fileName + "--文件上传失败");
        }
        // e.g. http://localhost:9090/files/1697438073596-avatar.png
        return "http://" + ip + ":" + port + "/files/" + fileName;
    }

    public void pullFile(String flag, HttpServletResponse response) {
        if (StrUtil.isEmpty(flag)) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        File file = new File(filePath + flag);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }

        try (OutputStream os = response.getOutputStream()) {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" +  URLEncoder.encode(flag, "UTF-8"));
            byte[] bytes = FileUtil.readBytes(file);
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    /**
     * 删除文件
     */
    public void deleteFile(String flag) throws IOException {
        if (StrUtil.isEmpty(flag)) {
            throw new IllegalArgumentException("文件标识不能为空");
        }

        File file = new File(filePath + flag);
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在: " + flag);
        }

        if (!file.delete()) {
            throw new IOException("文件删除失败: " + flag);
        }
    }

    /** 
     * 保存文件
     */
    public void replace(MultipartFile file) {
        try {
            FileUtil.writeBytes(file.getBytes(), filePath + file.getName());
        } catch (IORuntimeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String generateQR(String data) {
        if (data == null || data.equals("")) {
            throw new CustomException("二维码数据为空");
        }
        String url = ImgUtil.generateQR(data, filePath, ip, port);
        if (url == null) {
            throw new CustomException("二维码生成失败");
        }
        return url;
    }

}
