package com.example.controller;


import com.example.common.AutoLog;
import com.example.common.Result;
import com.example.dto.TraverseDTO;
import com.example.entity.Traverse;
import com.example.service.CareService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/DailyCare")
public class CareController {
    @Resource
    private CareService careService;

    @GetMapping("/SearchByHId/{id}")
    public Result SearchByHId(@PathVariable Integer id) {
        List<Traverse> list = careService.SearchByHId(id);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Traverse traverse,
                              @RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TraverseDTO> page = careService.selectPage(traverse,pageNum,pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectPageRecord")
    public Result selectPageRecord(Traverse traverse,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<TraverseDTO> page = careService.selectPageRecord(traverse,pageNum,pageSize);
        return Result.success(page);
    }

    @PutMapping("/update")
    @AutoLog("护理了一个病人")
    public Result update(@RequestBody Traverse traverse){
        careService.updateById(traverse);
        return Result.success();
    }

}
