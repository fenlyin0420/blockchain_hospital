package com.example.controller;


import com.example.common.Result;
import com.example.entity.Reserve;
import com.example.entity.Traverse;
import com.example.entity.Ward;
import com.example.service.BedsService;
import com.example.service.CareService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/DailyCare")
public class CareController {
    @Resource
    private CareService careService;

    @GetMapping("/search")
    public Result findAll(){
        List<Traverse> l = careService.findAll();
        return Result.success(l);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Traverse traverse){
        careService.updateById(traverse);
        return Result.success();
    }

}
