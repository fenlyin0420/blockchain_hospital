package com.example.controller;

import com.example.entity.Traverse;
import com.example.service.TraverseService;
import com.example.common.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/traverse")
public class TraverseController {
    @Resource
    private TraverseService traverseService;

    @GetMapping("/selectAll")
    public Result selectAll(Traverse traverse) {
        List<Traverse> list = traverseService.selectAll(traverse);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Traverse traverse,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Traverse> page = traverseService.selectPage(traverse, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Traverse traverse = traverseService.selectById(id);
        return Result.success(traverse);
    }

    @GetMapping("/selectByName")
    public Result selectById(String name) {
        Traverse traverse = traverseService.selectByName(name);
        return Result.success(traverse);
    }

    @GetMapping("/selectByDoctorId")
    public Result selectByDoctorId(Integer id) {
        Traverse traverse = traverseService.selectByDoctorId(id);
        return Result.success(traverse);
    }

    @GetMapping("/selectByHospitalId")
    public Result selectByHospitalId(Integer id) {
        Traverse traverse = traverseService.selectByHospitalId(id);
        return Result.success(traverse);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Traverse traverse) {
        traverseService.add(traverse);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Traverse traverse) {
        traverseService.updateById(traverse);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        traverseService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        traverseService.deleteBatch(ids);
        return Result.success();
    }
}
