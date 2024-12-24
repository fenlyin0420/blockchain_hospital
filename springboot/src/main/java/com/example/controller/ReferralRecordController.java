package com.example.controller;

import com.example.entity.ReferralRecord;
import com.example.service.ReferralRecordService;
import com.example.common.Result;
import com.example.common.enums.ReferalEnum;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/referral")
public class ReferralRecordController {
    @Resource
    private ReferralRecordService referalRecordService;

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(ReferralRecord referalRecord) {
        List<ReferralRecord> list = referalRecordService.selectAll(referalRecord);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(ReferralRecord referalRecord,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ReferralRecord> page = referalRecordService.selectPage(referalRecord, pageNum, pageSize);
        return Result.success(page);
    }


    @GetMapping("/selectById{id}")
    public Result selectById(@PathVariable Integer id) {
        ReferralRecord referalRecord = referalRecordService.selectById(id);
        return Result.success(referalRecord);
    }

    // 该接口即将被废弃！！！！！！ 不要使用！！！！！！
    @PutMapping("/update")
    public Result updateById(@RequestBody ReferralRecord referalRecord) {
        referalRecordService.updateById(referalRecord);
        return Result.success();
    }

    /**
     * 拒绝转出
     * @param referalRecord
     * @return
     */
    @PutMapping("/refuseOut")
    public Result refuseReferalOut(@RequestBody ReferralRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.REFUSED_BY_OUT_ADMIN.toString()); 
        return Result.success("已拒绝");
    }

    /**
     * 拒绝转入
     * @param referalRecord
     * @return
     */
    @PutMapping("/refuseIn")
    public Result refuseReferalIn(@RequestBody ReferralRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.REFUSED_BY_IN_ADMIN.toString()); 
        return Result.success("已拒绝");
    }

    /**
     * 同意转出，同时发送转诊记录到对方医院
     * @param referalRecord
     * @return
     */
    @PutMapping("/agreenOut")
    public Result agreenReferalOut(@RequestBody ReferralRecord referalRecord) {
        referalRecordService.send(referalRecord, "http://localhost:8091/referal/b/com"); 
        return Result.success("同意转入");
    }

    /**
     * 同意转入
     * @param referalRecord
     * @return
     */
    @PutMapping("/agreenIn")
    public Result agreenReferalIn(@RequestBody ReferralRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.WAIT_DOCTOR.toString());
        
        return Result.success("同意转出");
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

    /**
     *  
     * @param referalRecord
     * @return
     */
    @PostMapping("/add")
    public Result addNewReferalRecord(@RequestBody ReferralRecord referalRecord) {
        referalRecordService.add(referalRecord);
        return Result.success();
    }
}