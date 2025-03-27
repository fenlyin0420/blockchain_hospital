package com.example.service;

import com.example.dto.TraverseDTO;
import com.example.entity.Bed;
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

    public PageInfo<TraverseDTO> selectTraversePage(Traverse traverse, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TraverseDTO> list = bedsMapper.selectAllTraverse(traverse);
        return PageInfo.of(list);
    }

    public void updateById(TraverseDTO traverseDTO) {
        bedsMapper.updateById(traverseDTO);
    }

    public PageInfo<Bed> selectBedPage(Bed bed, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Bed> list = bedsMapper.selectBedPage(bed);
        return PageInfo.of(list);
    }

    public List<Bed> selectBedAll(Bed bed) {
        return bedsMapper.selectBedAll(bed);
    }

    public void updateBedById(Bed bed) {
        bedsMapper.updateBedById(bed);
    }
}
