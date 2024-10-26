package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.NurseMapper;
import com.example.utils.TokenUtils;
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

    public List<Nurse> findAll(){
        return nurseMapper.selectAll();
    }

    public List<Nurse> findBySearch(Params params){
        return nurseMapper.findBySearch(params);
    }

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

    public void add(Nurse nurse) {
        if(nurse.getPassword() == null){
            nurse.setPassword("123456");
        }
        nurseMapper.insertSelective(nurse);
    }

    /**
     * 根据ID查询
     */
    public Nurse selectById(Integer id) {
        return nurseMapper.selectById(id);
    }
}
