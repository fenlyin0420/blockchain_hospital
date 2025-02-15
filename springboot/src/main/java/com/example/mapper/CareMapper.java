package com.example.mapper;

import com.example.entity.Traverse;

import java.util.List;

public interface CareMapper {


    List<Traverse> SearchByHId(Integer id);

    int updateById(Traverse traverse);

    List<Traverse> selectPage(Traverse traverse);

    List<Traverse> selectPageRecord(Traverse traverse);
}
