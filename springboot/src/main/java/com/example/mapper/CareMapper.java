package com.example.mapper;

import com.example.dto.TraverseDTO;
import com.example.entity.Traverse;

import java.util.List;

public interface CareMapper {


    List<Traverse> SearchByHId(Integer id);

    int updateById(Traverse traverse);

    List<TraverseDTO> selectPage(Traverse traverse);

    List<TraverseDTO> selectPageRecord(Traverse traverse);
}
