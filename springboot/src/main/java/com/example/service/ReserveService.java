package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.CallEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Reserve;
import com.example.mapper.ReserveMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预约挂号表业务处理
 **/
@Slf4j
@Service
public class ReserveService {

    @Resource
    private ReserveMapper reserveMapper;

    /**
     * 新增
     */
    public void add(Reserve reserve) {
//        reserve.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        reserve.setStatus(CallEnum.STATUS_NO.status);
        reserveMapper.insert(reserve);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        reserveMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reserveMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Reserve reserve) {
        reserveMapper.updateById(reserve);
    }

    /**
     * 根据ID查询
     */
    public Reserve selectById(Integer id) {
        return reserveMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Reserve> selectAll(Reserve reserve) {
        return reserveMapper.selectAll(reserve);
    }

    /**
     * 分页查询
     */
    public PageInfo<Reserve> selectPage(Reserve reserve, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            reserve.setUserId(currentUser.getId());
        }
        if (RoleEnum.DOCTOR.name().equals(currentUser.getRole())) {
            reserve.setDoctorId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> list = reserveMapper.selectAll(reserve);

//        // 获取当前系统时间(医生只显示当日叫号)
//        LocalDateTime now = LocalDateTime.now();
//        // 定义一个日期时间格式化器
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        List<Reserve> listnow=new ArrayList<>();
//        String formattedDateTime = now.format(formatter);
//        for(Reserve i:list){
//            if(i.getTime().equals(formattedDateTime)){
//                listnow.add(i); // 移除就诊时间不再当日的元素
//            }
//        }
        return PageInfo.of(list);
    }

}