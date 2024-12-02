package com.example.service;

import com.example.entity.Traverse;
import com.example.entity.Ward;
import com.example.mapper.BedsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class BedsService {
    @Resource
    private BedsMapper bedsMapper;

    public PageInfo<Traverse> selectPage1(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Traverse> list = bedsMapper.selectAllTraverse(traverse);
        return PageInfo.of(list);
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
