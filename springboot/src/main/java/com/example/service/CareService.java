package com.example.service;

import com.example.entity.Traverse;
import com.example.mapper.CareMapper;
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
}
