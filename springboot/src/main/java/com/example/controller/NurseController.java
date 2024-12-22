package com.example.controller;

import com.example.entity.Nurse;
import com.example.service.NurseService;
import com.example.common.Result;

import com.github.pagehelper.PageInfo;
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
    @GetMapping("/selectByH/{id}")
        public Result selectByH(@PathVariable Integer id) {
            List<Nurse> list = nurseService.selectByH(id);
            return Result.success(list);
        }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Nurse nurse = nurseService.selectById(id);
        return Result.success(nurse);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Nurse nurse,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Nurse> page = nurseService.selectPage(nurse, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Nurse nurse) {
        nurseService.add(nurse);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Nurse nurse) {
        nurseService.updateById(nurse);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        nurseService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        nurseService.deleteBatch(ids);
        return Result.success();
    }


}
