package com.example.controller;

import com.example.common.AutoLog;
import com.example.common.Result;
import com.example.dto.TraverseDTO;
import com.example.entity.Bed;
import com.example.entity.Traverse;
import com.example.service.BedsService;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/AssignBeds")
public class BedsController {
    @Resource
    private BedsService bedsService;

    @GetMapping("/selectTraversePage")
    public Result selectTraversePage(Traverse traverse,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TraverseDTO> page = bedsService.selectTraversePage(traverse, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectBedPage")
    public Result selectBedPage(Bed bed,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Bed> page = bedsService.selectBedPage(bed, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectBedAll")
    public Result selectBedAll(Bed bed) {
        List<Bed> list = bedsService.selectBedAll(bed);
        return Result.success(list);
    }

    @PutMapping("/save")
    @AutoLog("给病人分配床位")
    public Result save(@RequestBody Traverse traverse){
        bedsService.updateById(traverse);
        return Result.success();
    }

    @PutMapping("/updateBedById")
    public Result updateBedById(@RequestBody Bed bed){
        bedsService.updateBedById(bed);
        return Result.success();
    }
}