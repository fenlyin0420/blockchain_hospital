package com.example.service;

import com.example.entity.Traverse;
import com.example.entity.Ward;
import com.example.mapper.BedsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BedsService {
    @Resource
    private BedsMapper bedsMapper;


    public List<Traverse> findAll() {
        return bedsMapper.selectAll();
    }

    public PageInfo<Ward> selectPage(Ward ward, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Ward> list = bedsMapper.selectAllWard(ward);
        return PageInfo.of(list);
    }

    public void updateById(Traverse traverse) {
        bedsMapper.updateById(traverse);
    }
}
