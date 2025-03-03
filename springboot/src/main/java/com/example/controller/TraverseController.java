package com.example.controller;

import com.example.common.AutoLog;
import com.example.entity.ReferralTraverse;
import com.example.entity.Traverse;
import com.example.service.TraverseService;
import com.example.common.Result;

import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;





@Slf4j
@RestController
@RequestMapping("/traverse")
public class TraverseController {
    @Resource
    private TraverseService traverseService;

    /**
     * 
     * @param traverse
     * @return
     */
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

    // 暂时不提供该接口哦，使用患者id查询
//     @GetMapping("/selectByName")
//     public Result selectById(String name) {
//         Traverse traverse = traverseService.selectByName(name);
//         return Result.success(traverse);
//     }

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

    @GetMapping("/canSend")
    public Result canSend(Integer doctorId) {
        List<Traverse> list =  traverseService.selectByCanSend(doctorId);
        return Result.success(list);
    }
    

    /**
     * 添加一份病历
     * @param traverse 病历数据
     * @return Result
     */
    @PostMapping("/add")
    @AutoLog("新增了一条病历")
    public Result add(@RequestBody Traverse traverse) {
        Integer id;
        try {
            id = traverseService.add(traverse);
        } catch(NullPointerException e) {
            return Result.error("病历信息不全（是否住院？）");
        }
        return Result.success(id);
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Traverse traverse) {
        traverseService.updateById(traverse);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @AutoLog("删除了一条病历")
    public Result deleteById(@PathVariable Integer id) {
        traverseService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        traverseService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 获取所有转诊后的病历
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/selectPageReferralTraverse")
    public Result selectPageReferralTraverse(@RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ReferralTraverse> page = traverseService.selectPageReferralTraverse(pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/insertReferralTraverse")
    public Result insertReferralTraverse(@RequestBody ReferralTraverse entity) {
        System.err.println(entity);
        traverseService.insertReferralTraverse(entity); 
        return Result.success("插入数据成功");
    }

    @PostMapping("/getSignData")
    public Result getSignData(@RequestBody Traverse traverse) {
        return Result.success(traverse.signData());
    }
    
    
}
