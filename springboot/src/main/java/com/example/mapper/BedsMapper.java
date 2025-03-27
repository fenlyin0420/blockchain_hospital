package com.example.mapper;

import com.example.dto.TraverseDTO;
import com.example.entity.Bed;
import com.example.entity.Traverse;
import com.example.entity.Ward;

import java.util.List;

public interface BedsMapper {


    List<TraverseDTO> selectAllTraverse(Traverse traverse);

    int updateById(TraverseDTO traverseDTO);

    List<Bed> selectBedPage(Bed bed);

    List<Bed> selectBedAll(Bed bed);

    void updateBedById(Bed bed);
}
