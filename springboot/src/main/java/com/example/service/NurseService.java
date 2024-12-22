package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.NurseMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *  护士业务处理
 */
@Service
public class NurseService {
    @Resource
    private NurseMapper nurseMapper;

    public Account login(Account account) {
        Account dbNurse = nurseMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbNurse)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbNurse.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }

        // 生成token
        String tokenData = dbNurse.getId() + "-" + RoleEnum.NURSE.name();
        String token = TokenUtils.createToken(tokenData, dbNurse.getPassword());
        dbNurse.setToken(token);
        return dbNurse;
    }

    /**
     * 根据ID查询
     */
    public Nurse selectById(Integer id) {
        return nurseMapper.selectById(id);
    }

    public List<Nurse> selectByH(Integer id) {
        return nurseMapper.selectByH(id);
    }


    public PageInfo<Nurse> selectPage(Nurse nurse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Nurse> list = nurseMapper.selectAll(nurse);
        return PageInfo.of(list);
    }

    public void add(Nurse nurse) {
        Nurse dbNurse = nurseMapper.selectByUsername(nurse.getUsername());
        if (ObjectUtil.isNotNull(dbNurse)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(nurse.getPassword())) {
            nurse.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(nurse.getName())) {
            nurse.setName(nurse.getUsername());
        } //这段代码没任何意义，用户名咋可能是空的呢，要是空的就查不出dbdoctor了  _2024/10/24 _sjh
        nurse.setRole(RoleEnum.NURSE.name());
        nurseMapper.insert(nurse);
    }

    public void updateById(Nurse nurse) {
        nurseMapper.updateById(nurse);
    }

    public void deleteById(Integer id) {
        nurseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            nurseMapper.deleteById(id);
        }
    }
}
