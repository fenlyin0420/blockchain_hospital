package com.example.controller;


import com.example.entity.Hospital;
import com.example.service.HospitalService;
import com.example.common.Result;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员前端操作接口
 **/

@RestController
@RequestMapping("/hospital")
public class HospitalController {
    @Resource
    private HospitalService hospitalService;

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Hospital> list = hospitalService.selectAll();
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Hospital> page = hospitalService.selectPage(pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Hospital hospital) {
        if(hospital.getId()==null){
            hospitalService.insert(hospital);
        }
        else {
            hospitalService.updateById(hospital);
        }
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        System.out.println("*************************************************"+id);
        hospitalService.deleteById(id);
        return Result.success();
    }



}