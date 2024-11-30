package com.example.mapper;


import com.example.entity.ReferalRecord;
import com.example.entity.Traverse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TraverseMapper {


    List<Traverse> selectAll(Traverse traverse);

    void add(Traverse traverse);

    void updateById(Traverse traverse);

    void deleteById(Integer id);

    Traverse selectById(Integer id);
    
    Traverse selectByTimestamp(String number);

    List<Traverse> selectByCanSend(Integer id);
}
