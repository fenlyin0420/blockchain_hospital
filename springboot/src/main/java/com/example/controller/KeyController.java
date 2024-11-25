package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Params;
import com.example.entity.RingSign;
import com.example.entity.Traverse;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.KeyService;
import com.example.service.UserService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.io.IOException;

import javax.annotation.Resource;
import javax.print.DocFlavor.READER;



@RestController
@RequestMapping("/keys")
public class KeyController {
    @Resource
    private KeyService keyService;
    @Resource
    private AdminService adminService;
    @Resource
    private DoctorService doctorService;
    @Resource
    private UserService userService;

    //查看个人信息
    @PostMapping("searchById")
    public Result searchById(@RequestBody Account account){
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            return Result.success(adminService.selectById(account.getId()));
        }
        if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            return Result.success(doctorService.selectById(account.getId()));
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            return Result.success(userService.selectById(account.getId()));
        }
        return Result.success();
    }
    //修改公钥和私钥
    @PutMapping("/updateKey")
    public Result updateKey(@RequestBody Account account){
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updateKey(account);
        }
        if (RoleEnum.DOCTOR.name().equals(account.getRole())) {
            doctorService.updateKey(account);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.updateKey(account);
        }
        return Result.success();
    }

    //进行环签名
    @PostMapping("/sign")
    public Result sign(@RequestBody Params params){
        RingSign ringSign=keyService.sign(params);
        return Result.success(ringSign);
    }
    //进行验签
    @PostMapping("/verifySign")
    public Result verifySign(@RequestBody Params params){
        RingSign ringSign=keyService.verifySign(params);
        return Result.success(ringSign);
    }

    //进行加密


    //进行解密
    @PostMapping("/decrypt")
    public Result decrypt(@RequestBody Traverse traverse){
        try {
            traverse = keyService.decrypt(traverse);
        } catch(CustomException e) {
            System.out.println(e.getMsg());
            return Result.error(e.getMsg());
        }
        return Result.success(traverse);
    }

    /**
     * 图片解密
     * @param imgURL 加密图片再服务器的url
     * @return 
     */
    @PostMapping("/imgDecrypt")
    public Result imgDecrypt(@RequestBody Traverse traverse) {
        if (" ".equals(traverse.getImg()) || traverse.getImg() == null || traverse.getImg().equals("")) {
            return Result.error("图像URL为空");
        }

        Object data;
        // String Url = traverse.getImg().substring(1, traverse.getImg().length() - 1);
        String Url = traverse.getImg();
        try {
            data = keyService.imgDecrypt(Url);
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return Result.error("图像传输失败");
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
            return Result.error("无法找到病历图片");
        } catch(WebClientRequestException e) {
            System.out.println(e.getMessage());
            return Result.error("图像URL无效，请检查病历");
        } catch(RuntimeException e) {
            System.out.println(e.getMessage());
            return Result.error("解密失败:(");
        }
        return Result.success(data);
    }
}
