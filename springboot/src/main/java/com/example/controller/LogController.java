package com.example.controller;

import com.example.common.Result;
import com.example.entity.Log;
import com.example.service.LogService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;


@RestController //表明是接口的入口
@RequestMapping("/log")
public class LogController {
    @Resource
    private LogService logService;

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Log log,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Log> page = logService.selectPage(log, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Log log) {
        logService.add(log);
        return Result.success();
    }



}
