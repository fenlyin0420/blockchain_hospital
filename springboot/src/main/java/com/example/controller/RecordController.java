package com.example.controller;

import com.example.common.Result;
import com.example.entity.Record;
import com.example.entity.Traverse;
import com.example.service.RecordService;
import com.example.dto.TraverseDTO;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

/**
 * 就诊记录表前端操作接口
 **/
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private RecordService recordService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Record record) {
        recordService.add(record);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        recordService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        recordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Record record) {
        recordService.updateById(record);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Record record = recordService.selectById(id);
        return Result.success(record);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Record record ) {
        List<Record> list = recordService.selectAll(record);
        return Result.success(list);
    }

    @GetMapping("/selectAllUserName")
    public Result selectAllUserName() {
        List<Record> list = recordService.selectAllUserName();
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Record record,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Record> page = recordService.selectPage(record, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 根据医生ID和住院状态查询患者
     */
    @GetMapping("/doctor")
    public Result selectByDoctorAndStatus(@RequestParam Integer doctorId,
                                        @RequestParam(defaultValue = "已住院") String inHospital) {
        List<TraverseDTO> records = recordService.selectByDoctorAndStatus(doctorId, inHospital);
        return Result.success(records);
    }

}