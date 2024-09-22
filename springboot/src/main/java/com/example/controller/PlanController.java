package com.example.controller;

import com.example.common.Result;
import com.example.entity.Plan;
import com.example.service.PlanService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 排班信息表前端操作接口
 **/
@RestController
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Plan plan) {
        planService.add(plan);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        planService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        planService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Plan plan) {
        planService.updateById(plan);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Plan plan = planService.selectById(id);
        return Result.success(plan);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Plan plan ) {
        List<Plan> list = planService.selectAll(plan);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Plan plan,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Plan> page = planService.selectPage(plan, pageNum, pageSize);
        return Result.success(page);
    }

}