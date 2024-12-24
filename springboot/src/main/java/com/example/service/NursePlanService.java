package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.nursePlan;
import com.example.exception.CustomException;
import com.example.mapper.NursePlanMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 排班信息表业务处理
 **/
@Service
public class NursePlanService {

    @Resource
    private NursePlanMapper nursePlanMapper;

    /**
     * 新增
     */
    public void add(nursePlan plan) {
        // 要先做一次这个排班的校验：同一个医生同一天只能有一条排班记录
        // 根据医生的id和日期查询一下有么有该记录
        nursePlan dbPlan = nursePlanMapper.selectByNurseIdAndDate(plan.getNurseId(), plan.getDate());
        if (ObjectUtil.isNotEmpty(dbPlan)) {
            throw new CustomException(ResultCodeEnum.PLAN_EXIST_ERROR);
        }
        nursePlanMapper.insert(plan);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        nursePlanMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            nursePlanMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(nursePlan plan) {
        nursePlanMapper.updateById(plan);
    }


    /**
     * 查询所有
     */
    public List<nursePlan> selectAll(nursePlan plan) {
        return nursePlanMapper.selectAll(plan);
    }

    /**
     * 分页查询
     */
    public PageInfo<nursePlan> selectPage(nursePlan plan, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 如果是护士，则只插叙护士自己的排班信息
        if (RoleEnum.NURSE.name().equals(currentUser.getRole())) {
            plan.setNurseId(currentUser.getId());
        }

        PageHelper.startPage(pageNum, pageSize); //开启分页查询
        List<nursePlan> list = nursePlanMapper.selectAll(plan);
        return PageInfo.of(list);
    }

}