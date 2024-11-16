package com.example.mapper;

import com.example.entity.Traverse;
import com.example.entity.Ward;

import java.util.List;

public interface BedsMapper {


    List<Traverse> selectAllTraverse(Traverse traverse);

    List<Ward> selectAllWard(Ward ward);

    int updateById(Traverse traverse);
}
