package com.example.controller;


import com.example.entity.ReferalRecord;
import com.example.service.ReferalRecordService;
import com.example.common.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/referal")
public class ReferalRecordController {
    @Resource
    private ReferalRecordService referalRecordService;

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ReferalRecord referalRecord) {
        List<ReferalRecord> list = referalRecordService.selectAll(referalRecord);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(ReferalRecord referalRecord,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ReferalRecord> page = referalRecordService.selectPage(referalRecord, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/selectById{id}")
    public Result selectById(@PathVariable Integer id) {
        ReferalRecord referalRecord = referalRecordService.selectById(id);
        return Result.success(referalRecord);
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.updateById(referalRecord);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        referalRecordService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        referalRecordService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/add")
    public Result add(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.add(referalRecord);
        return Result.success();
    }

    /**
     * 该接口只暴露给后端，作为接收转诊申请的接口
     * @return
     */
    @PostMapping("/b/com")
    public Result recieve(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.recieve(referalRecord);
        return Result.success("接收成功");
    }

}