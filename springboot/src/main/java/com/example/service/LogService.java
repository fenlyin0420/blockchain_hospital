package com.example.service;

import com.example.entity.Log;
import com.example.mapper.LogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LogService {
    @Resource
    private LogMapper logMapper;

    /**
     * 分页查询
     */
    public PageInfo<Log> selectPage(Log log, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Log> list = logMapper.selectAll(log);
        return PageInfo.of(list);
    }

    /**
     * 新增
     */
    public void add(Log log) {
        logMapper.insert(log);
    }
}
