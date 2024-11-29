package com.example.mapper;

import com.example.entity.Log;

import java.util.List;

public interface LogMapper {

    /**
     * 查询所有
     * */
    List<Log> selectAll(Log log);

    int insert(Log log);


}
