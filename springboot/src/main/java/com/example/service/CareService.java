package com.example.service;

import com.example.entity.Traverse;
import com.example.mapper.CareMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CareService {
    @Resource
    private CareMapper careMapper;

    public List<Traverse> findAll() {
        return careMapper.selectAll();
    }

    public void updateById(Traverse traverse) {
        careMapper.updateById(traverse);
    }
}
