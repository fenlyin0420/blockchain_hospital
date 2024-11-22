package com.example.controller;


import com.example.entity.ReferalRecord;
import com.example.service.ReferalRecordService;
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
@RequestMapping("/referal")
public class ReferralRecordController {
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

    // 该接口即将被废弃！！！！！！ 不要使用！！！！！！
    @PutMapping("/update")
    public Result updateById(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.updateById(referalRecord);
        return Result.success();
    }

    /**
     * 拒绝转出
     * @param referalRecord
     * @return
     */
    @PutMapping("/refuseOut")
    public Result refuseReferalOut(@RequestBody ReferalRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.REFUSED_BY_OUT_ADMIN.toString()); 
        return Result.success("已拒绝");
    }

    /**
     * 拒绝转入
     * @param referalRecord
     * @return
     */
    @PutMapping("/refuseIn")
    public Result refuseReferalIn(@RequestBody ReferalRecord referalRecord) {
        referalRecord.setResult(ReferalEnum.REFUSED_BY_IN_ADMIN.toString()); 
        return Result.success("已拒绝");
    }

    /**
     * 同意转出
     * @param referalRecord
     * @return
     */
    @PutMapping("/agreenOut")
    public Result agreenReferalOut(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.send(referalRecord, "http://localhost:8091/referal/b/com"); 
        return Result.success("同意转入");
    }

    /**
     * 同意转入
     * @param referalRecord
     * @return
     */
    @PutMapping("/agreenIn")
    public Result agreenReferalIn(@RequestBody ReferalRecord referalRecord) {
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
    public Result addNewReferalRecord(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.add(referalRecord);
        return Result.success();
    }

    /**
     * 该接口只暴露给后端，作为接收转诊申请的接口
     * @return
     */
    @PostMapping("/b/com")
    public Result recieveReferalRecord(@RequestBody ReferalRecord referalRecord) {
        referalRecordService.recieveReferalRecord(referalRecord);
        return Result.success("接收成功");
    }

    @GetMapping("/test")
    public String test() {
        return "This is a test";
    }
}