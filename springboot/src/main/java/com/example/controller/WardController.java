package com.example.controller;

import com.example.common.Result;
import com.example.entity.Doctor;
import com.example.entity.Ward;
import com.example.service.WardService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ward")
public class WardController {
    @Resource
    private WardService wardService;

    @GetMapping("/selectPage")
    public Result selectPage(Ward ward,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Ward> page = wardService.selectPage(ward, pageNum, pageSize);
        return Result.success(page);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        wardService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        wardService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/add")
    public Result add(@RequestBody Ward ward) {
        wardService.add(ward);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Ward ward) {
        wardService.updateById(ward);
        return Result.success();
    }


}
