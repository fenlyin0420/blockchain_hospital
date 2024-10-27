package com.example.controller;


import com.example.common.Result;
import com.example.entity.Nurse;
import com.example.entity.Traverse;
import com.example.service.BedsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/AssignBeds")
public class BedsController {
    @Resource
    private BedsService bedsService;

    @GetMapping("/search")
    public Result findAll(){
        List<Traverse> l = bedsService.findAll();
        return Result.success(l);
    }
}
