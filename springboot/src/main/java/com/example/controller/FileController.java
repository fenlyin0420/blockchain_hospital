package com.example.controller;

import com.example.utils.ImgUtil;
import com.example.utils.MyMultipartFile;
import com.example.common.Result;
import com.example.exception.CustomException;
import com.example.service.FileService;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.collection.CollUtil;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) {
        String url = fileService.save(file);
        return Result.success(url); 
    }


    /**
     * 获取文件（文件下载）
     *
     * @param flag
     * @param response
     */
     @GetMapping("/{flag}")   //  1697438073596-avatar.png
     public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        fileService.pullFile(flag, response);
     }

    /**
     * 删除文件
     *
     * @param flag
     */
    // @DeleteMapping("/{flag}")
    // public void delFile(@PathVariable String flag) {
    //     FileUtil.del(filePath +flag);
    //     System.out.println("删除文件" + flag + "成功");
    // }

    /**
     * wang-editor编辑器文件上传接口
     */
    // @PostMapping("/wang/upload")
    // public Map<String, Object> wangEditorUpload(MultipartFile file) {
    //     String flag = System.currentTimeMillis() + "";
    //     String fileName = file.getOriginalFilename();
    //     try {
    //         // 文件存储形式：时间戳-文件名
    //         FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
    //         System.out.println(fileName + "--上传成功");
    //         Thread.sleep(1L);
    //     } catch (Exception e) {
    //         System.err.println(fileName + "--文件上传失败");
    //     }
    //     String http = "http://" + ip + ":" + port + "/files/";
    //     Map<String, Object> resMap = new HashMap<>();
    //     // wangEditor上传图片成功后， 需要返回的参数
    //     resMap.put("errno", 0);
    //     resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + flag + "-" + fileName)));
    //     return resMap;
    // }

    /**
     * 生成二维码,数据为 seed
     * @param seed 二维码包含的数据
     * @return
     */
    @GetMapping("/generateQR")
    public Result generateQR(String data) {
        try {
            String url = fileService.generateQR(data);
            return Result.success(url);
        } catch (CustomException e) {
            return Result.error(e.getMsg());
        }
    }

    // @GetMapping("/getBase64")
    // public Result getBase64(@RequestParam String url) throws Exception {
    //     String base64 = ImgUtil.getImageBase64(ImgUtil.MultipartFileToBufferedImage(MyMultipartFile.fromURL(url)));
    //     return Result.success(base64);
    // }
}
