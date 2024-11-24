package com.example.mapper;


import com.example.entity.Drug;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DrugMapper {

    List<Drug> selectAll(Drug drug);


    void add(Drug drug);

    void updateById(Drug drug);

    void deleteById(Integer id);
}
