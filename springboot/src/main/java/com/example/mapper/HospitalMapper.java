package com.example.mapper;



import com.example.entity.Hospital;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作admin相关数据接口
*/
public interface HospitalMapper {


    List<Hospital> selectAll();


    void updateById(Hospital hospital);

    void delectById(Integer id);

    void insert(Hospital hospital);
}