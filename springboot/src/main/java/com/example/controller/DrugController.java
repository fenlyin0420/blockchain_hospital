package com.example.controller;


import com.example.common.AutoLog;
import com.example.common.Result;
import com.example.entity.Drug;
import com.example.service.DrugService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/drug")
public class DrugController {
    @Resource
    private DrugService drugService;

    @GetMapping("/selectAll")
    public Result selectAll(Drug drug) {
        List<Drug> list = drugService.selectAll(drug);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Drug drug,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Drug> page = drugService.selectPage(drug, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Drug drug = drugService.selectById(id);
        return Result.success(drug);
    }

    @GetMapping("/selectByNumber/{number}")
    public Result selectByNumber(@PathVariable String number) {
        Drug drug = drugService.selectByNumber(number);
        return Result.success(drug);
    }

    @PostMapping("/add")
    @AutoLog("新增药品信息")
    public Result add(@RequestBody Drug drug) {
        drugService.add(drug);
        return Result.success();
    }

    @PutMapping("/update")
    @AutoLog("修改药品信息")
    public Result updateById(@RequestBody Drug drug) {
        drugService.updateById(drug);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @AutoLog("删除药品信息")
    public Result deleteById(@PathVariable Integer id) {
        drugService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        drugService.deleteBatch(ids);
        return Result.success();
    }
}