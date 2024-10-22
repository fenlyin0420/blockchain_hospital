package com.example.controller;

import com.example.entity.Nurse;
import com.example.entity.Params;
import com.example.service.NurseService;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/nurse")
public class NurseController {
    @Resource
    private NurseService nurseService;  //尼玛，一定要引入资源啊，草泥马的这真卡死了，一定要长教训啊我日

    @GetMapping
    public Result findAll() {
        List<Nurse> l = nurseService.findAll();
        return Result.success(l);
    }

    @GetMapping("/research")
    public Result findBySearch(Params params) {
        List<Nurse> l = nurseService.findBySearch(params);
        return Result.success(l);
    }

    @PostMapping("/add")
    public Result save(@RequestBody Nurse nurse) {
        nurseService.add(nurse);
        return Result.success();
    }

}
