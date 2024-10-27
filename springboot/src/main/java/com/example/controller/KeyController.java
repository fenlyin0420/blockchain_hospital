package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Params;
import com.example.entity.RingSign;
import com.example.service.AdminService;
import com.example.service.DoctorService;
import com.example.service.KeyService;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static cn.hutool.core.lang.Console.log;

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
    public Result decrypt(@RequestBody Params params){
        return Result.success(keyService.decrypt(params));
    }
}
