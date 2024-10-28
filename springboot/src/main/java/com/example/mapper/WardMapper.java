package com.example.mapper;

import com.example.entity.Doctor;
import com.example.entity.Ward;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WardMapper {

    List<Ward> selectAll(Ward ward);


    int deleteById(Integer id);

    void insert(Ward ward);

    @Select("select * from ward where name = #{name}")
    Ward selectByName(String name);

    int updateById(Ward ward);

    @Select("select * from ward")
    List<Ward> findAll();
}
