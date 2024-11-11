package com.example.controller;

import com.example.entity.Nurse;
import com.example.entity.Params;
import com.example.service.NurseService;
import com.example.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController //表明是接口的入口
@RequestMapping("/nurse")
public class NurseController {
    @Resource
    private NurseService nurseService;  //尼玛，一定要引入资源啊，草泥马的这真卡死了，一定要长教训啊我日


    /**
     *根据护士所在医院的ID，查本医院护士
     */
    @GetMapping("/selectByH")
        public Result selectByH(Integer id) {
            List<Nurse> list = nurseService.selectByH(id);
            return Result.success(list);
        }

    @GetMapping
    public Result findAll() {
        List<Nurse> l = nurseService.findAll();
        return Result.success(l);
    }

    @GetMapping("/research")
    public Result findBySearch(Params params) {
        List<Nurse> l = nurseService.findBySearch(params);
        return Result.success(l);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Nurse nurse) {
        nurseService.updateById(nurse);
        return Result.success();
    }

//    @PostMapping("/add")
//    public Result save(@RequestBody Nurse nurse) {
//        nurseService.add(nurse);
//        return Result.success();
//    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Nurse nurse = nurseService.selectById(id);
        return Result.success(nurse);
    }



}
