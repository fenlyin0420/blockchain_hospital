package com.example.mapper;

import com.example.entity.Bed;
import com.example.entity.Traverse;
import com.example.entity.Ward;

import java.util.List;

public interface BedsMapper {


    List<Traverse> selectAllTraverse(Traverse traverse);

    int updateById(Traverse traverse);

    List<Bed> selectBedPage(Bed bed);

    List<Bed> selectBedAll(Bed bed);

    void updateBedById(Bed bed);
}
