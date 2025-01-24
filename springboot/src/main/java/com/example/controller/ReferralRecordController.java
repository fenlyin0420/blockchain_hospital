package com.example.controller;

import com.example.entity.ReferralRecord;
import com.example.service.ReferralRecordService;
import com.example.common.Result;
import com.example.common.enums.ReferralEnum;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/referral")
public class ReferralRecordController {
    @Resource
    private ReferralRecordService referralRecordService;

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ReferralRecord referralRecord) {
        List<ReferralRecord> list = referralRecordService.selectAll(referralRecord);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(ReferralRecord referralRecord,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ReferralRecord> page = referralRecordService.selectPage(referralRecord, pageNum, pageSize);
        return Result.success(page);
    }
    @GetMapping("/selectWaitingOut")
    public Result selectWaitingOut(ReferralRecord referralRecord) {
        referralRecord.setResult("待审批");
        List<ReferralRecord> list = referralRecordService.selectAll(referralRecord);
        return Result.success(list);
    }

    @GetMapping("/selectById{id}")
    public Result selectById(@PathVariable Integer id) {
        ReferralRecord referralRecord = referralRecordService.selectById(id);
        return Result.success(referralRecord);
    }

    @PutMapping("/update")
    public Result update(@RequestBody ReferralRecord referralRecord) {
        referralRecordService.update(referralRecord);
        return Result.success();
    }



    /**
     * 拒绝转入
     * @param referralRecord
     * @return
     */
    @PutMapping("/refuseIn")
    public Result refuseReferralIn(@RequestBody ReferralRecord referralRecord) {
        referralRecord.setResult(ReferralEnum.REFUSED_BY_IN_ADMIN.toString()); 
        return Result.success("已拒绝");
    }

    /**
     * 同意转入
     * @param referralRecord
     * @return
     */
    @PutMapping("/agreenIn")
    public Result agreenReferralIn(@RequestBody ReferralRecord referralRecord) {
        referralRecord.setResult(ReferralEnum.WAIT_DOCTOR.toString());
        
        return Result.success("同意转出");
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        referralRecordService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        referralRecordService.deleteBatch(ids);
        return Result.success();
    }

    /**
     *  
     * @param referralRecord
     * @return
     */
    @PostMapping("/add")
    public Result addNewReferralRecord(@RequestBody ReferralRecord referralRecord) {
        referralRecordService.add(referralRecord);
        return Result.success();
    }
}