package com.example.controller;


import com.example.common.Result;
import com.example.entity.Traverse;
import com.example.entity.Ward;
import com.example.service.BedsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/selectPage")
    public Result selectPage(Ward ward,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Ward> page = bedsService.selectPage(ward, pageNum, pageSize);
        return Result.success(page);
    }

    @PutMapping("/save")
    public Result save(@RequestBody Traverse traverse){
        bedsService.updateById(traverse);
        return Result.success();
    }
}
