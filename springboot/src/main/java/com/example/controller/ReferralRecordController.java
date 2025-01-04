package com.example.controller;

import com.example.entity.ReferralRecord;
import com.example.service.ReferralRecordService;
import com.example.common.Result;
import com.example.common.enums.ReferalEnum;

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


    @GetMapping("/selectById{id}")
    public Result selectById(@PathVariable Integer id) {
        ReferralRecord referralRecord = referralRecordService.selectById(id);
        return Result.success(referralRecord);
    }

    // 该接口即将被废弃！！！！！！ 不要使用！！！！！！
    @PutMapping("/update")
    public Result updateById(@RequestBody ReferralRecord referralRecord) {
        referralRecordService.updateById(referralRecord);
        return Result.success();
    }

    /**
     * 拒绝转出
     * @param referralRecord
     * @return
     */
    @PutMapping("/refuseOut")
    public Result refuseReferalOut(@RequestBody ReferralRecord referralRecord) {
        referralRecord.setResult(ReferalEnum.REFUSED_BY_OUT_ADMIN.toString());
        return Result.success("已拒绝");
    }

    /**
     * 拒绝转入
     * @param referralRecord
     * @return
     */
    @PutMapping("/refuseIn")
    public Result refuseReferalIn(@RequestBody ReferralRecord referralRecord) {
        referralRecord.setResult(ReferalEnum.REFUSED_BY_IN_ADMIN.toString());
        return Result.success("已拒绝");
    }

    /**
     * 同意转出
     * @param referralRecord
     * @return
     */
    @PutMapping("/agreenOut")
    public Result agreenReferalOut(@RequestBody ReferralRecord referralRecord) {
        referralRecordService.send(referralRecord, "http://localhost:8091/referal/b/com");
        return Result.success("同意转入");
    }

    /**
     * 同意转入
     * @param referralRecord
     * @return
     */
    @PutMapping("/agreenIn")
    public Result agreenReferalIn(@RequestBody ReferralRecord referralRecord) {
        referralRecord.setResult(ReferalEnum.WAIT_DOCTOR.toString());
        
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
    public Result addNewReferalRecord(@RequestBody ReferralRecord referralRecord) {
        referralRecordService.add(referralRecord);
        return Result.success();
    }

    /**
     * 该接口只暴露给后端，作为接收转诊申请的接口
     * @return
     */
    @PostMapping("/b/com")
    public Result recieveReferalRecord(@RequestBody ReferralRecord referralRecord) {
        referralRecordService.recieveReferalRecord(referralRecord);
        return Result.success("接收成功");
    }

    @GetMapping("/test")
    public String test() {
        return "This is a test";
    }
}