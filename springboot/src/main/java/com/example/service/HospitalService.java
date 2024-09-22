package com.example.service;



import com.example.entity.Hospital;
import com.example.mapper.HospitalMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员业务处理
 **/
@Service
public class HospitalService {

    @Resource
    private HospitalMapper hospitalMapper;


    public List<Hospital> selectAll() {
        return hospitalMapper.selectAll();
    }

    public PageInfo<Hospital> selectPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Hospital> list = hospitalMapper.selectAll();
        return PageInfo.of(list);
    }

    public void updateById(Hospital hospital) {
        hospitalMapper.updateById(hospital);
    }

    public void deleteById(Integer id) {
        hospitalMapper.delectById(id);
    }

    public void insert(Hospital hospital) {
        hospitalMapper.insert(hospital);
    }
}