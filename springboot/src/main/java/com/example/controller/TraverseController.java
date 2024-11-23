package com.example.controller;

import com.example.common.AutoLog;
import com.example.common.enums.InhospitalEnum;
import com.example.entity.Traverse;
import com.example.service.TraverseService;
import com.example.common.Result;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    // @GetMapping("/selectByName")
    // public Result selectById(String name) {
    //     Traverse traverse = traverseService.selectByName(name);
    //     return Result.success(traverse);
    // }

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

    /**
     * 添加一份病历
     * @param traverse 病历数据
     * @return Result
     */
    @PostMapping("/add")
    @AutoLog("新增了一条病历")
    public Result add(@RequestBody Traverse traverse) {
        try {
            if(traverse.getInHospital().equals("是")){
                traverse.setInHospital(InhospitalEnum.Inhospital_YES.status);
            }
            if(traverse.getInHospital().equals("否")){
                traverse.setInHospital(InhospitalEnum.Inhospital_NO.status);
            }
            traverseService.add(traverse);
        } catch(NullPointerException e) {
            return Result.error("病历信息不全（是否住院？）");
        }
        return Result.success();
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
}
