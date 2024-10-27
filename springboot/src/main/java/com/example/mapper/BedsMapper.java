package com.example.mapper;

import com.example.entity.Traverse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BedsMapper {


    List<Traverse> selectAll();
}
