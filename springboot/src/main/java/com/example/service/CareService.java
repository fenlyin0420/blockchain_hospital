package com.example.service;

import com.example.entity.Traverse;
import com.example.mapper.CareMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class CareService {
    @Resource
    private CareMapper careMapper;

    public List<Traverse> SearchByHId(Integer id) {
        return careMapper.SearchByHId(id);
    }

    public void updateById(Traverse traverse) {
        careMapper.updateById(traverse);
    }

    public PageInfo<Traverse> selectPage(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Traverse> list = careMapper.selectPage(traverse);
        return PageInfo.of(list);
    }

    public PageInfo<Traverse> selectPageRecord(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Traverse> list = careMapper.selectPageRecord(traverse);
        return PageInfo.of(list);
    }
}
