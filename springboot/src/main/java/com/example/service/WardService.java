package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Doctor;
import com.example.entity.Ward;
import com.example.exception.CustomException;
import com.example.mapper.WardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WardService {
    @Resource
    private WardMapper wardMapper;


    public PageInfo<Ward> selectPage(Ward ward, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Ward> list = wardMapper.selectAll(ward);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        wardMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            wardMapper.deleteById(id);
        }
    }

    public void add(Ward ward) {
        Ward dbward = wardMapper.selectByName(ward.getName());
        if (ObjectUtil.isNotNull(dbward)) {
            throw new CustomException(ResultCodeEnum.WARD_EXIST_ERROR);
        }
        wardMapper.insert(ward);
    }

    public void updateById(Ward ward) {
        wardMapper.updateById(ward);
    }
}
