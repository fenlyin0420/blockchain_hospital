package com.example.controller;

import com.example.common.Result;
import com.example.entity.nursePlan;
import com.example.service.NursePlanService;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 排班信息表前端操作接口
 **/
@RestController
@RequestMapping("/nursePlan")
public class NursePlanController {

    @Resource
    private NursePlanService nursePlanService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody nursePlan plan) {
        nursePlanService.add(plan);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        nursePlanService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        nursePlanService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody nursePlan plan) {
        nursePlanService.updateById(plan);
        return Result.success();
    }



    /**
     * 根据ID查询
     */


    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(nursePlan plan ) {
        List<nursePlan> list = nursePlanService.selectAll(plan);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(nursePlan plan,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<nursePlan> page = nursePlanService.selectPage(plan, pageNum, pageSize);
        return Result.success(page);
    }

}