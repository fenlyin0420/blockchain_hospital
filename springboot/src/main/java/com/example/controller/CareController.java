package com.example.controller;


import com.example.common.AutoLog;
import com.example.common.Result;
import com.example.entity.Traverse;
import com.example.service.CareService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/DailyCare")
public class CareController {
    @Resource
    private CareService careService;

    @GetMapping("/SearchByHId/{id}")
    public Result SearchByHId(@PathVariable Integer id) {
        System.out.println("Received ID: " + id);
        List<Traverse> l = careService.SearchByHId(id);
        return Result.success(l);
    }

    @PutMapping("/update")
    @AutoLog("日常护理")
    public Result update(@RequestBody Traverse traverse){
        careService.updateById(traverse);
        return Result.success();
    }

}
