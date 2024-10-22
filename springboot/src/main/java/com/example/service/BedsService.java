package com.example.service;

import com.example.entity.Traverse;
import com.example.mapper.BedsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BedsService {
    @Resource
    private BedsMapper bedsMapper;


    public List<Traverse> findAll() {
        System.out.println("1[23");
        return bedsMapper.selectAll();
    }
}
